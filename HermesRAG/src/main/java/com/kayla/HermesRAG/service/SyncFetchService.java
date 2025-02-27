package com.kayla.HermesRAG.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Service
@RequiredArgsConstructor
public class SyncFetchService { // 자동 실행되는 fetch 메서드

    private final FetchLogService fetchLogService;
    private final VectorizerService vectorizerService;

    @Scheduled(cron = "0 10 00 * * ?") // 매일 자정 10분에 실행
    public void scheduledFetchArticles() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Time: " + now.format(formatter));
        System.out.println("Starting Data Synchronization");

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = fetchLogService.getStartDate(yesterday); // 가져올 시작 날짜

        if (!startDate.isAfter(yesterday)) {
            // 데이터 가져오고 벡터화까지
            int processed_count = vectorizerService.fetchAndVectorize(startDate, yesterday);
            System.out.println(processed_count + " Data Synchronization Complete!");
        } else {
            System.out.println("Up to date, no new data.");
        }
    }

    // 애플리케이션이 실행된 후 자동 실행
    @EventListener(ApplicationReadyEvent.class)
    public void fetchAtStart() {
        System.out.println("Application Start");
        System.out.println("Starting Data Synchronization");

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = fetchLogService.getStartDate(yesterday); // 가져올 시작 날짜

        if (!startDate.isAfter(yesterday)) {
            // 데이터 가져오고 벡터화까지
            int processed_count = vectorizerService.fetchAndVectorize(startDate, yesterday);
            System.out.println(processed_count + " Data Synchronization Complete!");
        } else {
            System.out.println("Up to date, no new data.");
        }
    }

}
