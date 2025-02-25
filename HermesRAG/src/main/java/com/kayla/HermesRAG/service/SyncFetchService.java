package com.kayla.HermesRAG.service;

import jakarta.transaction.Transactional;
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

    private final FetchService fetchService;
    private final FetchLogService fetchLogService;

    @Transactional
    @Scheduled(cron = "0 10 00 * * ?") // 매일 자정 10분에 실행
    public void scheduledFetchArticles() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Time: " + now.format(formatter));
        System.out.println("Starting Data Synchronization");

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = fetchLogService.getStartDate(yesterday); // 가져올 시작 날짜

        if (!startDate.isAfter(yesterday)) {
            fetchService.fetchArticles(startDate, yesterday); // 주기적으로 기사 가져오기
            System.out.println("Data Synchronization Complete!");
        } else {
            System.out.println("Up to date, no new data.");
        }
    }

    // 애플리케이션이 실행된 후 자동 실행
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void fetchAtStart() {
        System.out.println("Application Start");
        System.out.println("Starting Data Synchronization");

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = fetchLogService.getStartDate(yesterday); // 가져올 시작 날짜

        if (!startDate.isAfter(yesterday)) {
            fetchService.fetchArticles(startDate, yesterday);
            System.out.println("Data Synchronization Complete!");
        } else {
            System.out.println("Up to date, no new data.");
        }
    }

}
