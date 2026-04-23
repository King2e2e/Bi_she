package org.example.mapper;

import org.apache.ibatis.annotations.Select;
import org.example.entity.SysUser;

/**
 * 系统用户数据访问层。
 * 负责对 sys_user 表进行增删改查操作。
 */
public interface SysUserMapper {

    /** 按用户名查询用户 */
    @Select("select * from sys_user where username = #{username} and deleted = 0 limit 1")
    SysUser selectByUsername(String username);

    /** 按手机号查询用户 */
    @Select("select * from sys_user where phone = #{phone} and deleted = 0 limit 1")
    SysUser selectByPhone(String phone);

    /** 按邮箱查询用户 */
    @Select("select * from sys_user where email = #{email} and deleted = 0 limit 1")
    SysUser selectByEmail(String email);

    /** 新增用户 */
    int insert(SysUser user);

    /** 更新最后登录时间 */
    int updateLastLoginTime(SysUser user);
}
