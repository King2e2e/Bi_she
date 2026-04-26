package org.example.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Service
public class InferenceService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${python.infer.base-url:http://localhost:5000}")
    private String pythonInferBaseUrl;

    @SuppressWarnings("unchecked")
    public Map<String, Object> callInfer(File imageFile, Double conf) {
        String url = pythonInferBaseUrl + "/infer";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(imageFile));
        if (conf != null) {
            body.add("conf", String.valueOf(conf));
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        Map<String, Object> response = restTemplate.postForObject(url, requestEntity, Map.class);
        return response == null ? java.util.Collections.emptyMap() : response;
    }
}
