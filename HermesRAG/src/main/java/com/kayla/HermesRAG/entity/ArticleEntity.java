package com.kayla.HermesRAG.entity;

import jakarta.persistence.*;
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
    @Column(length = 500)
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

    @Column(name = "web_url", length = 500)
    private String webUrl;

    @Column(name = "api_url", length = 500)
    private String apiUrl;

    @Column(name = "trail_text", columnDefinition = "TEXT")
    private String trailText;

//    @Column(name = "is_hosted")
//    private boolean isHosted;

    @Column(name = "pillar_id")
    private String pillarId;

    @Column(name = "pillar_name")
    private String pillarName;

    // 새로 추가된 컬럼
    @Lob // BLOB 타입으로 지정
    private byte[] webTitleEmbedding;

    @Lob // BLOB 타입으로 지정
    private byte[] trailTextEmbedding;
}
