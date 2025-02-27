package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.dto.ArticleCoreDTO;
import com.kayla.HermesRAG.service.VectorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vectorize")
@RequiredArgsConstructor
public class VectorizeController {

    private final VectorizeService vectorizeService;

    @GetMapping("/get")
    public List<ArticleCoreDTO> getMonthArticles() {
        return vectorizeService.getMonthArticles();
    }


    // 아래 vectorize 함수들은 Scheduled 아닌 수동 실행

    @GetMapping("/initial")
    public HttpStatus vectorizeInitialArticles() { // 초기 실행시 어제부터 한달치 기사 가져오기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate monthAgo = yesterday.minusMonths(1);
        // 벡터화까지
        int processed_count = vectorizeService.fetchAndVectorize(monthAgo, yesterday);  // 문제 있으면 함수 안에서 오류 던짐
        System.out.println(processed_count + " Data Vectorize Complete!");
        return HttpStatus.OK;
    }

    @GetMapping("/yesterday")
    public HttpStatus vectorizeYesterdayArticles() { // 어제 기사 가져오기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int processed_count = vectorizeService.fetchAndVectorize(yesterday, yesterday);
        System.out.println(processed_count + " Data Vectorize Complete!");
        return HttpStatus.OK;
    }

}
