package org.example.mapper;

import org.apache.ibatis.annotations.Select;
import org.example.entity.Admin;

import java.util.List;

/**
 * 管理员数据访问层，负责和数据库直接交互。
 */
public interface AdminMapper {

    /** 新增管理员 */
    int insert(Admin admin);

    /** 按 ID 删除管理员 */
    int deleteById(Integer id);

    /** 更新管理员 */
    int updateById(Admin admin);

    /** 按 ID 查询管理员 */
    Admin selectById(Integer id);

    /** 查询所有管理员 */
    List<Admin> selectAll(Admin admin);

    /** 按用户名查询管理员 */
    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);
}
