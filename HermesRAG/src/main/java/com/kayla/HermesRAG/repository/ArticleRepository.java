package com.kayla.HermesRAG.repository;

import com.kayla.HermesRAG.dto.FaissLoadDTO;
import com.kayla.HermesRAG.dto.SearchRequestDTO;
import com.kayla.HermesRAG.dto.VecRequestDTO;
import com.kayla.HermesRAG.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    // JPQL 사용하면 엔티티를 중간에 매핑하지 않고 직접 DTO를 반환
    @Query("SELECT new com.kayla.HermesRAG.dto.VecRequestDTO(a.id, a.webTitle, a.trailText, a.webUrl, a.webPublicationDate) " +
            "FROM ArticleEntity a " +
            "WHERE a.webPublicationDate >= :oneMonthAgo")
    List<VecRequestDTO> findArticlesFromLastMonthAsVecRequestDTO(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);


    @Query("SELECT new com.kayla.HermesRAG.dto.SearchRequestDTO(a.id, a.webTitle, a.trailText, a.webUrl, a.webTitleEmbedding, a.trailTextEmbedding) " +
            "FROM ArticleEntity a " +
            "WHERE a.webTitleEmbedding IS NOT NULL " +
            "AND a.trailTextEmbedding IS NOT NULL " +
            "AND a.webPublicationDate >= :oneMonthAgo")
    List<SearchRequestDTO> findRecentArticlesWithEmbeddings(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);


    @Query("SELECT new com.kayla.HermesRAG.dto.FaissLoadDTO(a.id, a.webTitleEmbedding, a.trailTextEmbedding) " +
            "FROM ArticleEntity a " +
            "WHERE a.webTitleEmbedding IS NOT NULL " +
            "AND a.trailTextEmbedding IS NOT NULL " +
            "AND a.webPublicationDate >= :oneMonthAgo")
    List<FaissLoadDTO> findRecentEmbeddings(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);

}
