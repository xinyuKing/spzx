package com.xinyuKing.spzx.manager.service;

import com.xinyuKing.spzx.model.dto.system.LoginDto;
import com.xinyuKing.spzx.model.entity.system.SysUser;
import com.xinyuKing.spzx.model.vo.system.LoginVo;

public interface SysUserService {
    /*用户登录*/
    LoginVo login(LoginDto loginDto);

    SysUser getUserinfo(String token);

    void logout(String token);
}
