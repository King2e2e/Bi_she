package org.example.service;

import jakarta.annotation.Resource;
import org.example.entity.ViolationAlert;
import org.example.mapper.ViolationAlertMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ViolationAlertService {

    @Resource
    private ViolationAlertMapper violationAlertMapper;

    public void save(ViolationAlert alert) {
        LocalDateTime now = LocalDateTime.now();
        if (alert.getAlertTime() == null) {
            alert.setAlertTime(now);
        }
        alert.setCreateTime(now);
        alert.setUpdateTime(now);
        if (alert.getStatus() == null) {
            alert.setStatus(0);
        }
        violationAlertMapper.insert(alert);
    }

    public List<ViolationAlert> listAll() {
        return violationAlertMapper.selectAll();
    }

    public void deleteById(Long id) {
        ViolationAlert alert = null;
        try {
            List<ViolationAlert> alerts = violationAlertMapper.selectAll();
            for (ViolationAlert item : alerts) {
                if (item.getId() != null && item.getId().equals(id)) {
                    alert = item;
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        violationAlertMapper.deleteById(id);
        deleteImageFile(alert);
    }

    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<ViolationAlert> alerts = violationAlertMapper.selectAll();
        for (Long id : ids) {
            ViolationAlert found = null;
            for (ViolationAlert item : alerts) {
                if (item.getId() != null && item.getId().equals(id)) {
                    found = item;
                    break;
                }
            }
            deleteImageFile(found);
        }
        violationAlertMapper.deleteByIds(ids);
    }

    private void deleteImageFile(ViolationAlert alert) {
        if (alert == null || alert.getImagePath() == null || alert.getImagePath().isBlank()) {
            return;
        }
        try {
            String relativePath = alert.getImagePath();
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
            File file = new File(System.getProperty("user.dir"), relativePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ignored) {
        }
    }
}
