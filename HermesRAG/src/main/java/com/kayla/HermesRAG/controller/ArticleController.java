package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.dto.ArticleSummaryDTO;
import com.kayla.HermesRAG.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor //final이나 @NonNull 붙은 필드만을 파라미터로 받는 생성자를 자동으로 생성
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/test")
    public HttpStatus test() {
        return HttpStatus.OK;
    }

    @GetMapping("/fetch")
    public HttpStatus fetchArticles() {
        return articleService.fetchWeekAndSaveArticles(); // 서비스에서 반환된 상태 코드 사용
    }

    @GetMapping("/get")
    public List<ArticleSummaryDTO> getArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/search")
    public String searchArticles(@RequestParam(value = "query") String query) {
        try {
            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // 가상 환경을 활성화 + Python 스크립트를 실행
            String command = "cmd /c \"src\\python\\venv\\Scripts\\activate"
                    + " && python src\\python\\similarity-search.py \"" + query + "\"";

            // ProcessBuilder로 명령 실행 준비
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
            processBuilder.directory(new java.io.File("C:\\Users\\ykcho\\Desktop\\HermesRAG\\HermesRAG"));

            // Python 스크립트 실행
            Process process = processBuilder.start();

            // 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return result.toString();
            } else {
                return "Error executing Python script";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
