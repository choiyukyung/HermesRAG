package com.kayla.HermesRAG.service;

import com.kayla.HermesRAG.dto.ArticleCoreDTO;
import com.kayla.HermesRAG.dto.VectorDTO;
import com.kayla.HermesRAG.entity.ArticleEntity;
import com.kayla.HermesRAG.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<ArticleCoreDTO> getAllArticles() {
        List<ArticleEntity> articles = articleRepository.findAll();
        return articles.stream()
                .map(article -> new ArticleCoreDTO(article.getId(), article.getWebTitle(), article.getTrailText()))
                .collect(Collectors.toList());
    }

    public List<ArticleCoreDTO> getMonthArticles() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate monthAgo = yesterday.minusMonths(1);

        LocalDateTime monthAgoTime = monthAgo.atStartOfDay();
        List<ArticleEntity> articles = articleRepository.findByWebPublicationDateAfter(monthAgoTime);

        return articles.stream()
                .map(article -> new ArticleCoreDTO(article.getId(), article.getWebTitle(), article.getTrailText()))
                .collect(Collectors.toList());
    }

    public List<VectorDTO> getRecentVectorsEmbeddings() {
        return articleRepository.findVectorsWithEmbeddings();
    }
}
