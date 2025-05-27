package com.kayla.HermesRAG.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayla.HermesRAG.dto.VecRequestDTO;
import com.kayla.HermesRAG.dto.VecResponseDTO;
import com.kayla.HermesRAG.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorizeService {
    private final FetchService fetchService;
    private final ArticleRepository articleRepository;

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
            // FastAPI 서버에 HTTP 요청
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/run-vectorize";

            String jsonOutput = restTemplate.getForObject(url, String.class);

            // JSON 파싱 후 DTO 변환
            return objectMapper.readValue(jsonOutput, VecResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FastAPI 호출 실패", e);
        }
    }

    public List<VecRequestDTO> getMonthVecRequestDTO() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime monthAgoTime = yesterday.minusMonths(1).atStartOfDay();
        return articleRepository.findArticlesFromLastMonthAsVecRequestDTO(monthAgoTime);
    }

}
