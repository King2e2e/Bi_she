package org.example.controller;

import jakarta.annotation.Resource;
import org.example.common.Result;
import org.example.service.InferenceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inference")
public class InferenceController {

    @Resource
    private InferenceService inferenceService;

    @PostMapping("/infer")
    public Result infer(@RequestParam("image") MultipartFile image,
                        @RequestParam(value = "conf", required = false) Double conf) throws IOException {
        File tempFile = File.createTempFile("infer-", ".jpg");
        image.transferTo(tempFile);
        try {
            Map<String, Object> response = inferenceService.callInfer(tempFile, conf);
            return Result.success(response);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
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
