package com.kayla.HermesRAG.repository;

import com.kayla.HermesRAG.dto.VectorDTO;
import com.kayla.HermesRAG.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    List<ArticleEntity> findByWebPublicationDateAfter(LocalDateTime oneMonthAgo);

    @Query("SELECT new com.kayla.HermesRAG.dto.VectorDTO(a.id, a.webTitleEmbedding, a.trailTextEmbedding) " +
            "FROM ArticleEntity a " +
            "WHERE a.webTitleEmbedding IS NOT NULL " +
            "AND a.trailTextEmbedding IS NOT NULL ")
    List<VectorDTO> findVectorsWithEmbeddings();
}
