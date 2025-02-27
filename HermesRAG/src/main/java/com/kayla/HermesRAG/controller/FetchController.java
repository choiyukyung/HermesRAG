package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.service.VectorizerService;
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

    private final VectorizerService vectorizerService;

    // 컨트롤러의 아래 fetch 함수들은 Scheduled 아닌 수동 실행

    @GetMapping("/initial")
    public HttpStatus fetchInitialArticles() { // 초기 실행시 어제부터 한달치 기사 가져오기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate monthAgo = yesterday.minusMonths(1);
        // 데이터 가져오고 벡터화까지
        vectorizerService.fetchAndVectorize(monthAgo, yesterday);  // 문제 있으면 함수 안에서 오류 던짐
        return HttpStatus.OK;
    }

    @GetMapping("/yesterday")
    public HttpStatus fetchYesterdayArticles() { // 어제 기사 가져오기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        vectorizerService.fetchAndVectorize(yesterday, yesterday);
        return HttpStatus.OK;
    }

}
