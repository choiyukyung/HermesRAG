package com.kayla.HermesRAG.service;

import com.kayla.HermesRAG.dto.GuardianApiDTO;
import com.kayla.HermesRAG.entity.ArticleEntity;
import com.kayla.HermesRAG.entity.FetchLogEntity;
import com.kayla.HermesRAG.repository.ArticleRepository;
import com.kayla.HermesRAG.repository.FetchLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
@RequiredArgsConstructor
public class FetchService {

    private final ArticleRepository articleRepository;
    private final FetchLogRepository fetchLogRepository;

    private final RestTemplate restTemplate;

    @Value("${guardian.api.url}")
    private String guardianApiUrl;

    @Value("${guardian.api.key}")
    private String guardianApiKey;

    @Transactional
    @Scheduled(cron = "0 10 00 * * ?") // 매일 자정 10분에 실행
    public void scheduledFetchArticles() {

        LocalDate yesterday = LocalDate.now().minusDays(1);

        // 빠진 날짜 있는지 확인
        FetchLogEntity lastLog = fetchLogRepository.findTopByOrderByLastFetchedDateDesc();
        LocalDate lastFetchedDate = (lastLog != null) ? lastLog.getLastFetchedDate() : LocalDate.MIN;

        // 한 달 전 vs 마지막 날짜 다음 날
        LocalDate oneMonthAgo = yesterday.minusMonths(1);
        LocalDate startDate = lastFetchedDate.isBefore(oneMonthAgo) ? oneMonthAgo : lastFetchedDate.plusDays(1);

        // 빠진 날짜를 고려해서 주기적으로 동안의 기사 가져오기
        fetchArticles(startDate, yesterday);

        // 마지막으로 가져온 날짜를 로그에 저장
        saveFetchLog(yesterday);
    }

    @Transactional // 메서드 실행을 하나의 트랜잭션으로 처리
    public HttpStatus fetchArticles(LocalDate from_date, LocalDate to_date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String from_date_string = from_date.format(formatter);
            String to_date_string = to_date.format(formatter);

            // 페이지네이션
            int page = 1;
            int totalPages = 1;
            int page_size = 200; // 크면 api 요청 수 감소.

            List<ArticleEntity> allArticles = new ArrayList<>();
            String url = "";
            while (page <= totalPages) {
                // Guardian API 호출
                url = guardianApiUrl + "?api-key=" + guardianApiKey
                        + "&section=technology|business|science&type=article" // IT 관련 기사만
                        + "&from-date=" + from_date_string + "&to-date=" + to_date_string
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

            // 마지막으로 저장한 날짜
            saveFetchLog(to_date);

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


    // FetchLog
    @Transactional
    public void saveFetchLog(LocalDate lastFetchedDate) {
        FetchLogEntity fetchLog = new FetchLogEntity();
        fetchLog.setLastFetchedDate(lastFetchedDate);
        fetchLogRepository.save(fetchLog);
    }
}
