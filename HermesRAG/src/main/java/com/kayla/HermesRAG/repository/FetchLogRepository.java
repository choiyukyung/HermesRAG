package com.kayla.HermesRAG.repository;

import com.kayla.HermesRAG.entity.FetchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FetchLogRepository extends JpaRepository<FetchLogEntity, String> {
    FetchLogEntity findTopByOrderByLastFetchedDateDesc();
}
