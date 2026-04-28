package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.entity.SysUser;

import java.util.List;

/**
 * 系统用户数据访问层。
 * 负责对 sys_user 表进行增删改查操作。
 */
public interface SysUserMapper {

    SysUser selectByUsername(String username);

    SysUser selectByPhone(String phone);

    SysUser selectByEmail(String email);

    int insert(SysUser user);

    int updateLastLoginTime(SysUser user);

    List<SysUser> selectAll(@Param("keyword") String keyword);
}
