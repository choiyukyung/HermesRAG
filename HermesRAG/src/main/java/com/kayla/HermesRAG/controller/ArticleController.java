package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.dto.ArticleCoreDTO;
import com.kayla.HermesRAG.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor // final이나 @NonNull 붙은 필드만을 파라미터로 받는 생성자를 자동으로 생성
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/test")
    public String test() {
        return "Hello React!";
    }

    @GetMapping("/core")
    public List<ArticleCoreDTO> getArticleCores() {
        return articleService.getAllArticleCores();
    }

}
