package org.example.controller;

import jakarta.annotation.Resource;
import org.example.common.Result;
import org.example.entity.ViolationAlert;
import org.example.service.ViolationAlertService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/violationAlert")
public class ViolationAlertController {

    @Resource
    private ViolationAlertService violationAlertService;

    @PostMapping("/save")
    public Result save(@RequestBody ViolationAlert alert) {
        violationAlertService.save(alert);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<ViolationAlert> list = violationAlertService.listAll();
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        violationAlertService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        violationAlertService.deleteByIds(ids);
        return Result.success();
    }
}
