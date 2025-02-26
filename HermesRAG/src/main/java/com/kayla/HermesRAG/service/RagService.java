package com.kayla.HermesRAG.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayla.HermesRAG.dto.RagResponseDTO;
import com.kayla.HermesRAG.utils.PythonExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {

    private final PythonExecutor pythonExecutor;
    private final ObjectMapper objectMapper;

    public RagResponseDTO summarize(String query) {
        try {
            String jsonOutput = pythonExecutor.runPythonScript("src\\python\\rag_summarize.py", query);

            // JSON 파싱 후 DTO 변환
            return objectMapper.readValue(jsonOutput, RagResponseDTO.class);
        } catch (Exception e) {
            return new RagResponseDTO("error", null, "Internal Server Error");
        }
    }
}
