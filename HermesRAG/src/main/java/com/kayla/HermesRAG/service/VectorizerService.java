package com.kayla.HermesRAG.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayla.HermesRAG.dto.VecResponseDTO;
import com.kayla.HermesRAG.utils.PythonExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VectorizerService {
    private final PythonExecutor pythonExecutor;
    private final ObjectMapper objectMapper;

    public VecResponseDTO vectorize() {
        try {
            String jsonOutput = pythonExecutor.runPythonScript("src\\python\\vectorize.py");

            // JSON 파싱 후 DTO 변환
            return objectMapper.readValue(jsonOutput, VecResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Internal Server Error", e);
        }
    }
}
