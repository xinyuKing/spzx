package com.xinyuKing.spzx.manager.service.Impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.xinyuKing.spzx.manager.service.ValidateCodeService;
import com.xinyuKing.spzx.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public ValidateCodeVo generateValidateCode() {
        //通过hutool生成验证码

        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 20);
        String codeValue = circleCaptcha.getCode();//4位验证码
        String imageBase64 = circleCaptcha.getImageBase64();//验证码图片（使用Base64编码）

        //将验证码存到redis中，设置redis中的key：uuid redis的value：验证码值
        //设置过期时间
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set("user:validate:"+key,codeValue,60*2,TimeUnit.SECONDS);

        //封装成ValidateCodeVo返回
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64,"+imageBase64);

        return validateCodeVo;
    }
}
