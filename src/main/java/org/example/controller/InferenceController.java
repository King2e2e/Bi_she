package org.example.controller;

import jakarta.annotation.Resource;
import org.example.common.Result;
import org.example.entity.ViolationAlert;
import org.example.service.InferenceService;
import org.example.service.ViolationAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inference")
public class InferenceController {

    @Resource
    private InferenceService inferenceService;

    @Resource
    private ViolationAlertService violationAlertService;

    @PostMapping("/infer")
    public Result infer(@RequestParam("image") MultipartFile image,
                        @RequestParam(value = "conf", required = false) Double conf) throws IOException {
        File tempFile = File.createTempFile("infer-", ".jpg");
        image.transferTo(tempFile);
        try {
            Map<String, Object> response = inferenceService.callInfer(tempFile, conf);
            Object dataObj = response.get("data");
            Map<?, ?> data = dataObj instanceof Map ? (Map<?, ?>) dataObj : null;
            String imagePath = saveSnapshot(tempFile);
            if (data != null) {
                Object detectionsObj = data.get("detections");
                if (detectionsObj instanceof List<?> detections && !detections.isEmpty()) {
                    for (Object item : detections) {
                        if (item instanceof Map<?, ?> detMap) {
                            String type = String.valueOf(detMap.get("label"));
                            if (!isViolation(type)) {
                                continue;
                            }
                            ViolationAlert alert = new ViolationAlert();
                            alert.setSource("live-monitor");
                            alert.setType(type);
                            alert.setRiskLevel("高危");
                            alert.setDescription(buildDescription(type));
                            alert.setImagePath(imagePath);
                            Object confObj = detMap.get("confidence");
                            if (confObj != null) {
                                try {
                                    alert.setConfidence(Double.valueOf(confObj.toString()));
                                } catch (Exception ignored) {
                                }
                            }
                            alert.setAlertTime(LocalDateTime.now());
                            alert.setStatus(0);
                            violationAlertService.save(alert);
                        }
                    }
                }
            }
            return Result.success(response);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private boolean isViolation(String type) {
        return "smoke".equalsIgnoreCase(type) || "drink".equalsIgnoreCase(type)
                || "吸烟".equals(type) || "喝酒".equals(type);
    }

    private String buildDescription(String type) {
        if ("smoke".equalsIgnoreCase(type) || "吸烟".equals(type)) {
            return "检测到吸烟违规行为";
        }
        if ("drink".equalsIgnoreCase(type) || "喝酒".equals(type)) {
            return "检测到饮酒违规行为";
        }
        return "检测到违规行为：" + type;
    }

    private String saveSnapshot(File tempFile) {
        try {
            File dir = new File(System.getProperty("user.dir"), "uploads/violations");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            Path target = new File(dir, fileName).toPath();
            Files.copy(tempFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/violations/" + fileName;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/health")
    public Result health() {
        Map<String, Object> data = new HashMap<>();
        data.put("service", "spring-boot-inference-proxy");
        data.put("status", "ok");
        return Result.success(data);
    }
}
