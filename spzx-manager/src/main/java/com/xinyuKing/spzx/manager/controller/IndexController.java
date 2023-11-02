package com.xinyuKing.spzx.manager.controller;

import com.xinyuKing.spzx.manager.service.SysUserService;
import com.xinyuKing.spzx.manager.service.ValidateCodeService;
import com.xinyuKing.spzx.model.dto.system.LoginDto;
import com.xinyuKing.spzx.model.entity.system.SysUser;
import com.xinyuKing.spzx.model.vo.common.Result;
import com.xinyuKing.spzx.model.vo.common.ResultCodeEnum;
import com.xinyuKing.spzx.model.vo.system.LoginVo;
import com.xinyuKing.spzx.model.vo.system.ValidateCodeVo;
import com.xinyuKing.spzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidateCodeService validateCodeService;

    /*用户退出*/
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(name = "token")String token){
        sysUserService.logout(token);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    
    /*获取登录用户信息*/
    /*@GetMapping(value = "/getUserInfo")
    public Result getUserInfo(@RequestHeader(name = "token")String token){
        //通过token查询redis中的用户信息
        SysUser sysUser = sysUserService.getUserinfo(token);
        //返回用户信息
        return Result.build(sysUser,ResultCodeEnum.SUCCESS);
    }*/
    /*优化：从ThreadLocal中读取*/
    @GetMapping(value = "/getUserInfo")
    public Result getUserInfo(){
        //返回用户信息
        return Result.build(AuthContextUtil.get(),ResultCodeEnum.SUCCESS);
    }

    /*生成图片验证码*/
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode(){
        ValidateCodeVo validateCodeVo=validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);
    }

    /*用户登录方法*/
    @Operation(summary = "登录的方法")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto){
        LoginVo loginVo=sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }
}
