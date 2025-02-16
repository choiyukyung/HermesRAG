package com.kayla.HermesRAG.repository;

import com.kayla.HermesRAG.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {
}
