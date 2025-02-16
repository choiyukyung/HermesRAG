package com.kayla.HermesRAG.service;

import com.kayla.HermesRAG.dto.GuardianApiDTO;
import com.kayla.HermesRAG.dto.GuardianApiResponseDTO;
import com.kayla.HermesRAG.entity.ArticleEntity;
import com.kayla.HermesRAG.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final RestTemplate restTemplate;

    @Value("${guardian.api.url}")
    private String guardianApiUrl;

    @Value("${guardian.api.key}")
    private String guardianApiKey;

    @Transactional // 메서드 실행을 하나의 트랜잭션으로 처리
    public HttpStatus fetchWeekAndSaveArticles() {
        try {
            // Guardian API 호출
            String url = guardianApiUrl + "?api-key=" + guardianApiKey;
            GuardianApiDTO guardianApiDTO = restTemplate.getForObject(url, GuardianApiDTO.class);

            List<ArticleEntity> articles = getArticleEntities(guardianApiDTO);
            for (ArticleEntity article : articles) {
                articleRepository.save(article); // 트랜잭션 적용
            }

            return HttpStatus.OK;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch and save articles", e);
        }
    }

    private static List<ArticleEntity> getArticleEntities(GuardianApiDTO guardianApiDTO) {
        if (guardianApiDTO == null || guardianApiDTO.getResponse() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No response found");
        }

        GuardianApiResponseDTO responseDTO = guardianApiDTO.getResponse();
        if (responseDTO.getResults().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No articles found");
        }

        return responseDTO.getResults();
    }

}
