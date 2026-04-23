package org.example.controller;

import jakarta.annotation.Resource;
import org.example.common.Result;
import org.example.entity.SysUser;
import org.example.service.SysUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户认证控制器。
 * 提供登录和注册接口给前端调用。
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /** 用户注册 */
    @PostMapping("/register")
    public Result register(@RequestBody SysUser user) {
        sysUserService.register(user);
        return Result.success();
    }

    /** 用户登录 */
    @PostMapping("/login")
    public Result login(@RequestBody SysUser user) {
        SysUser loginUser = sysUserService.login(user);
        return Result.success(loginUser);
    }
}
