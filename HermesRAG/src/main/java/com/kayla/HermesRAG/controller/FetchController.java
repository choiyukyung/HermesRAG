package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.service.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/fetch")
@RequiredArgsConstructor
public class FetchController {

    private final FetchService fetchService;

    // 컨트롤러의 아래 fetch 함수들은 Scheduled 아닌 수동 실행

    @GetMapping("/initial")
    public HttpStatus fetchInitialArticles() { // 초기 실행시 어제부터 한달치 기사 가져오기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate monthAgo = yesterday.minusMonths(1);
        return fetchService.fetchArticles(monthAgo, yesterday); // 서비스에서 반환된 상태 코드 사용
    }

    @GetMapping("/yesterday")
    public HttpStatus fetchYesterdayArticles() { // 어제 기사 가져오기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return fetchService.fetchArticles(yesterday, yesterday);
    }

}
