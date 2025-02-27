package com.kayla.HermesRAG.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayla.HermesRAG.dto.VecResponseDTO;
import com.kayla.HermesRAG.utils.PythonExecutor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VectorizerService {
    private final FetchService fetchService;

    private final PythonExecutor pythonExecutor;
    private final ObjectMapper objectMapper;

    public int fetchAndVectorize(LocalDate from_date, LocalDate to_date) {
        fetchService.fetchArticles(from_date, to_date);

        // 데이터베이스에 커밋되었는지 확인하기 위한 짧은 지연
        try {
            Thread.sleep(1000); // 1초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        VecResponseDTO vecResponseDTO = vectorize();
        return vecResponseDTO.getProcessed_count();
    }

    @Transactional
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
