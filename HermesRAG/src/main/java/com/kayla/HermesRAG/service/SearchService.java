package com.kayla.HermesRAG.service;

import com.kayla.HermesRAG.dto.SearchRequestDTO;
import com.kayla.HermesRAG.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ArticleRepository articleRepository;

    public List<SearchRequestDTO> getRecentVectorsEmbeddings() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate monthAgo = yesterday.minusMonths(1);

        LocalDateTime monthAgoTime = monthAgo.atStartOfDay();
        return articleRepository.findRecentVectorsWithEmbeddings(monthAgoTime);
    }
}
