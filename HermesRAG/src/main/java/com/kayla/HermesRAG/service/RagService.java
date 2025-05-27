package com.kayla.HermesRAG.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayla.HermesRAG.dto.RagResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RagService {

    private final ObjectMapper objectMapper;

    public RagResponseDTO summarize(String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/run-rag-summary";

            String jsonOutput = restTemplate.getForObject(url, String.class);

            // JSON 파싱 후 DTO 변환
            return objectMapper.readValue(jsonOutput, RagResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FastAPI 호출 실패", e);
        }
    }

    public RagResponseDTO answer(String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/run-rag-answer";

            String jsonOutput = restTemplate.getForObject(url, String.class);

            // JSON 파싱 후 DTO 변환
            return objectMapper.readValue(jsonOutput, RagResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FastAPI 호출 실패", e);
        }
    }
}
