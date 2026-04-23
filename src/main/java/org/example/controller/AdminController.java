package org.example.controller;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.common.Result;
import org.example.entity.Admin;
import org.example.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器层，负责接收前端请求并返回统一结果。
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /** 新增管理员 */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }

    /** 删除管理员 */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }

    /** 批量删除管理员 */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids);
        return Result.success();
    }

    /** 修改管理员 */
    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return Result.success();
    }

    /** 按 ID 查询管理员 */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    /** 查询所有管理员 */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    /** 分页查询 */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> page = adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(page);
    }
}
