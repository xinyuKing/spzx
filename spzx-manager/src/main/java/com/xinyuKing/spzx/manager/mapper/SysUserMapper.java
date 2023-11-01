package com.xinyuKing.spzx.manager.mapper;

import com.xinyuKing.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {

    /*根据用户名查询用户*/
    SysUser selectUserInfoByUserName(String userName);
}
