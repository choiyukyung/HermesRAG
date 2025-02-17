package com.kayla.HermesRAG.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String type;

    @Column(name = "section_id")
    private String sectionId;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "web_publication_date")
    private LocalDateTime webPublicationDate;

    @Column(name = "web_title")
    private String webTitle;

    @Column(name = "web_url")
    private String webUrl;

    @Column(name = "api_url")
    private String apiUrl;

    @Column(name = "trail_text", columnDefinition = "TEXT")
    private String trailText;

//    @Column(name = "is_hosted")
//    private boolean isHosted;

    @Column(name = "pillar_id")
    private String pillarId;

    @Column(name = "pillar_name")
    private String pillarName;
}
