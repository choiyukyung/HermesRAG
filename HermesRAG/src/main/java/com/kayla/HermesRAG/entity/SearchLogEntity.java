package com.kayla.HermesRAG.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_log")
@Setter
public class SearchLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String query;

    @Column(name = "result_type")
    private String resultType; // "summary" 또는 "answer"

    @Column(name = "searched_at")
    private LocalDateTime searchedAt;
}
