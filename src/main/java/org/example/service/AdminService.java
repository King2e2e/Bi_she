package org.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.common.Constants;
import org.example.common.enums.ResultCodeEnum;
import org.example.common.enums.RoleEnum;
import org.example.entity.Account;
import org.example.entity.Admin;
import org.example.exception.CustomException;
import org.example.mapper.AdminMapper;
import org.example.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员业务层，负责处理登录、注册、增删改查等业务逻辑。
 */
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 新增管理员。
     * 这里统一在业务层处理默认值和重复校验，避免控制器里写业务代码。
     */
    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (ObjectUtil.isNotNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }

        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }

        // 如果前端没有传用户名，直接报错，避免插入空数据。
        if (ObjectUtil.isEmpty(admin.getUsername())) {
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }

        // 如果姓名为空，默认使用用户名。
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }

        admin.setRole(RoleEnum.ADMIN.name());
        adminMapper.insert(admin);
    }

    /** 删除 */
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    /** 批量删除 */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            adminMapper.deleteById(id);
        }
    }

    /** 修改 */
    public void updateById(Admin admin) {
        adminMapper.updateById(admin);
    }

    /** 根据ID查询 */
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    /** 查询所有 */
    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    /** 分页查询 */
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    /**
     * 登录。
     * 登录成功后返回用户信息，并附带 token 给前端保存。
     */
    public Account login(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isEmpty(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        String tokenData = dbAdmin.getId() + "-" + RoleEnum.ADMIN.name();
        String token = TokenUtils.createToken(tokenData, dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    /**
     * 注册。
     * 先把前端提交的公共字段复制到管理员对象，再走新增逻辑。
     */
    public void register(Account account) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(account, admin);
        add(admin);
    }

    /** 修改密码 */
    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbAdmin.setPassword(account.getNewpassword());
        adminMapper.updateById(dbAdmin);
    }
}
