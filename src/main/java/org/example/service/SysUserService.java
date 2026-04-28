package org.example.service;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.example.common.enums.ResultCodeEnum;
import org.example.entity.SysUser;
import org.example.exception.CustomException;
import org.example.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统用户业务层。
 * 负责注册、登录等认证相关业务。
 */
@Service
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 用户注册。
     * 这里先做最基础的重复校验与默认值处理，保证前端能够正常调用。
     */
    public void register(SysUser user) {
        if (ObjectUtil.isEmpty(user.getUsername()) || ObjectUtil.isEmpty(user.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }

        if (ObjectUtil.isNotNull(sysUserMapper.selectByUsername(user.getUsername()))) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isNotEmpty(user.getPhone()) && ObjectUtil.isNotNull(sysUserMapper.selectByPhone(user.getPhone()))) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isNotEmpty(user.getEmail()) && ObjectUtil.isNotNull(sysUserMapper.selectByEmail(user.getEmail()))) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }

        user.setNickname(ObjectUtil.isEmpty(user.getNickname()) ? user.getUsername() : user.getNickname());
        user.setRole(ObjectUtil.isEmpty(user.getRole()) ? "user" : user.getRole());
        user.setStatus(1);
        user.setDeleted(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.insert(user);
    }

    /**
     * 用户登录。
     * 这里先按用户名+密码匹配，后续如果你要我可以再升级成加密密码版本。
     */
    public SysUser login(SysUser user) {
        if (ObjectUtil.isEmpty(user.getUsername()) || ObjectUtil.isEmpty(user.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }

        SysUser dbUser = sysUserMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        if (ObjectUtil.isNotNull(dbUser.getStatus()) && dbUser.getStatus() == 0) {
            throw new CustomException("401", "账号已被禁用");
        }

        dbUser.setPassword(null);
        return dbUser;
    }

    public List<SysUser> selectAll(String keyword) {
        return sysUserMapper.selectAll(keyword);
    }
}
