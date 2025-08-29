package com.kayla.HermesRAG.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayla.HermesRAG.dto.RagResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RagService {

    private final ObjectMapper objectMapper;

    public RagResponseDTO summarize(String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("query", query);

            //MediaType.APPLICATION_FORM_URLENCODED + MultiValueMap → POST form-data 전송
            //한글, 특수문자 자동 인코딩 처리

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            String url = "http://fastapi:8000/run-rag-summary";
            String responseBody = restTemplate.postForObject(url, request, String.class);

            return objectMapper.readValue(responseBody, RagResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FastAPI 호출 실패", e);
        }
    }

    public RagResponseDTO answer(String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("query", query);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            String url = "http://fastapi:8000/run-rag-answer";
            String responseBody = restTemplate.postForObject(url, request, String.class);

            return objectMapper.readValue(responseBody, RagResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FastAPI 호출 실패", e);
        }
    }
}
