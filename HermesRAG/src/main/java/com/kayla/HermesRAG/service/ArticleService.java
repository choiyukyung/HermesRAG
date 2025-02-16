package com.kayla.HermesRAG.service;

import com.kayla.HermesRAG.dto.GuardianApiDTO;
import com.kayla.HermesRAG.entity.ArticleEntity;
import com.kayla.HermesRAG.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            // 일주일
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(7);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayString = today.format(formatter);
            String weekAgoString = weekAgo.format(formatter);

            // 페이지네이션
            int page = 1;
            int totalPages = 1;
            int page_size = 100; // 크면 api 요청 수 줄어들음.

            List<ArticleEntity> allArticles = new ArrayList<>();
            String url = "";
            while (page <= totalPages) {
                // Guardian API 호출
                url = guardianApiUrl + "?api-key=" + guardianApiKey
                        + "&from-date=" + weekAgoString + "&to-date=" + todayString
                        + "&page=" + page + "&page-size=" + page_size
                        + "&show-fields=trailText"; // 각 기사의 요약정보

                GuardianApiDTO guardianApiDTO = restTemplate.getForObject(url, GuardianApiDTO.class);

                List<ArticleEntity> articles = getArticleEntities(guardianApiDTO);
                allArticles.addAll(articles);

                // 총 페이지 수는 요청을 보내면 알 수 있다.
                totalPages = guardianApiDTO.getResponse().getPages();
                page++;
            }
            System.out.println("url: " + url);

            // DB에 저장
            for (ArticleEntity article : allArticles) {
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

        GuardianApiDTO.Response responseDTO = guardianApiDTO.getResponse();
        if (responseDTO.getResults().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No articles found");
        }

        List<ArticleEntity> articles = new ArrayList<>();
        responseDTO.getResults().forEach(dto -> {
            ArticleEntity article = new ArticleEntity();
            article.setId(dto.getId());
            article.setType(dto.getType());
            article.setSectionId(dto.getSectionId());
            article.setSectionName(dto.getSectionName());
            article.setWebPublicationDate(dto.getWebPublicationDate());
            article.setWebTitle(dto.getWebTitle());
            article.setWebUrl(dto.getWebUrl());
            article.setApiUrl(dto.getApiUrl());
            article.setTrailText(dto.getFields() != null ? dto.getFields().getTrailText() : null);
            article.setPillarId(dto.getPillarId());
            article.setPillarName(dto.getPillarName());

            articles.add(article);
        });

        return articles;
    }

}
