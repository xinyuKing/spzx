package com.xinyuKing.spzx.manager.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xinyuKing.spzx.common.exception.XinyuKingException;
import com.xinyuKing.spzx.manager.mapper.SysUserMapper;
import com.xinyuKing.spzx.manager.service.SysUserService;
import com.xinyuKing.spzx.model.dto.system.LoginDto;
import com.xinyuKing.spzx.model.entity.system.SysUser;
import com.xinyuKing.spzx.model.vo.common.ResultCodeEnum;
import com.xinyuKing.spzx.model.vo.system.LoginVo;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /*用户登录*/
    @Override
    public LoginVo login(LoginDto loginDto) {
        //验证验证码
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();
        String redisCaptcha = redisTemplate.opsForValue().get("user:validate:" + codeKey);
        //不一致(比较时要注意不区分大小写)，返回信息
        if (StrUtil.isEmpty(redisCaptcha) || !StrUtil.equalsIgnoreCase(captcha,redisCaptcha)){
            throw  new XinyuKingException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //如果一致删除redis中的验证码
        redisTemplate.delete("user:validate:" + codeKey);

        //获取提交的用户名，loginDto中获取
        String userName = loginDto.getUserName();

        //根据用户名查询用户
        SysUser sysUser=sysUserMapper.selectUserInfoByUserName(userName);

        //若根据用户名查不到用户，返回错误信息
        if (sysUser==null){
//            throw new RuntimeException("用户不存在");
            throw new XinyuKingException(ResultCodeEnum.LOGIN_ERROR);
        }

        //若根据用户名查到了用户，用户存在
        //获取输入的密码，比较输入的密码和数据的密码是否一致（没有设置salt，后续可以优化）
        String databasePassword = sysUser.getPassword();
        String inputPassword = loginDto.getPassword();
        //将输入密码进行加密，再进行比较(md5加密可以封装成工具类)
        inputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if (!inputPassword.equals(databasePassword)){
//            throw new RuntimeException("密码错误");
            throw new XinyuKingException(ResultCodeEnum.LOGIN_ERROR);
        }

        //密码一致，登录成功，反之登录失败
        //登录成功，生成用户唯一标识token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        //把登录成功的用户信息存到redis中
        //key:token   value:用户信息
        redisTemplate.opsForValue().set("user:login:"+token,JSON.toJSONString(sysUser),7, TimeUnit.DAYS);

        //返回loginVo对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    @Override
    public SysUser getUserinfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login:" + token);
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }
}
