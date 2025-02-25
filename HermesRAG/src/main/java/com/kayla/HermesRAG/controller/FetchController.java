package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.service.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetch")
@RequiredArgsConstructor
public class FetchController {

    private final FetchService fetchService;

    @GetMapping("/initial")
    public HttpStatus fetchInitialArticles() { // 초기 실행시 한달치 기사 가져오기
        return fetchService.fetchInitialArticles(); // 서비스에서 반환된 상태 코드 사용
    }

}
