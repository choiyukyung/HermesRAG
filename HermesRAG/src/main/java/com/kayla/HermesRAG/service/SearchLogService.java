package com.kayla.HermesRAG.service;

import com.kayla.HermesRAG.entity.SearchLogEntity;
import com.kayla.HermesRAG.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    public void saveLog(String query, String resultType) {
        SearchLogEntity log = new SearchLogEntity();
        log.setQuery(query);
        log.setResultType(resultType);
        log.setSearchedAt(LocalDateTime.now());
        searchLogRepository.save(log);
    }
}
