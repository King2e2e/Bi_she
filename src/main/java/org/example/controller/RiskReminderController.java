package org.example.controller;

import jakarta.annotation.Resource;
import org.example.common.Result;
import org.example.entity.RiskReminder;
import org.example.service.RiskReminderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/riskReminder")
public class RiskReminderController {

    @Resource
    private RiskReminderService riskReminderService;

    @PostMapping("/save")
    public Result save(@RequestBody RiskReminder reminder) {
        riskReminderService.save(reminder);
        return Result.success();
    }

    @PostMapping("/pushBatch")
    public Result pushBatch(@RequestBody Map<String, Object> body) {
        Object userIdsObj = body.get("userIds");
        Long alertId = body.get("alertId") == null ? null : Long.valueOf(body.get("alertId").toString());
        String title = body.get("title") == null ? "风险提醒" : body.get("title").toString();
        String content = body.get("content") == null ? "" : body.get("content").toString();

        List<Long> userIds = new java.util.ArrayList<>();
        if (userIdsObj instanceof List<?> userIdsRaw) {
            for (Object item : userIdsRaw) {
                if (item != null) {
                    userIds.add(Long.valueOf(item.toString()));
                }
            }
        }

        riskReminderService.pushBatch(userIds, alertId, title, content);
        return Result.success();
    }

    @GetMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Long userId) {
        List<RiskReminder> list = riskReminderService.selectByUserId(userId);
        return Result.success(list);
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(riskReminderService.selectAll());
    }

    @PostMapping("/markRead/{id}")
    public Result markRead(@org.springframework.web.bind.annotation.PathVariable Long id) {
        riskReminderService.markRead(id);
        return Result.success();
    }

    @PostMapping("/markAllRead")
    public Result markAllRead(@RequestParam Long userId) {
        riskReminderService.markAllRead(userId);
        return Result.success();
    }

    @PostMapping("/delete/{id}")
    public Result delete(@org.springframework.web.bind.annotation.PathVariable Long id) {
        riskReminderService.deleteById(id);
        return Result.success();
    }
}
