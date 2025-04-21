package com.kayla.HermesRAG.repository;

import com.kayla.HermesRAG.entity.SearchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchLogRepository extends JpaRepository<SearchLogEntity, Long> {
}
