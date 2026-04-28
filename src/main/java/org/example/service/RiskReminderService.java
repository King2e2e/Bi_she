package org.example.service;

import jakarta.annotation.Resource;
import org.example.entity.RiskReminder;
import org.example.mapper.RiskReminderMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskReminderService {

    @Resource
    private RiskReminderMapper riskReminderMapper;

    public void save(RiskReminder reminder) {
        LocalDateTime now = LocalDateTime.now();
        if (reminder.getReadStatus() == null) {
            reminder.setReadStatus(0);
        }
        if (reminder.getCreateTime() == null) {
            reminder.setCreateTime(now);
        }
        reminder.setUpdateTime(now);
        riskReminderMapper.insert(reminder);
    }

    public List<RiskReminder> selectByUserId(Long userId) {
        return riskReminderMapper.selectByUserId(userId);
    }

    public List<RiskReminder> selectAll() {
        return riskReminderMapper.selectAll();
    }

    public void markRead(Long id) {
        riskReminderMapper.updateReadStatus(id, 1);
    }

    public void markAllRead(Long userId) {
        riskReminderMapper.updateReadStatusByUserId(userId, 1);
    }

    public void deleteById(Long id) {
        riskReminderMapper.deleteById(id);
    }

    public void pushBatch(List<Long> userIds, Long alertId, String title, String content) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        List<RiskReminder> reminders = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Long userId : userIds) {
            RiskReminder reminder = new RiskReminder();
            reminder.setUserId(userId);
            reminder.setAlertId(alertId);
            reminder.setTitle(title);
            reminder.setContent(content);
            reminder.setReadStatus(0);
            reminder.setCreateTime(now);
            reminder.setUpdateTime(now);
            reminders.add(reminder);
        }
        for (RiskReminder reminder : reminders) {
            riskReminderMapper.insert(reminder);
        }
    }
}
