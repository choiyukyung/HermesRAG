package com.kayla.HermesRAG.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RagResponseDTO {

    private String status;
    private String message;
    private List<RetrievedArticle> articles;

    @Getter
    @Setter
    public static class RetrievedArticle {
        private String id;

        @JsonProperty("web_title")
        private String webTitle;

        @JsonProperty("trail_text")
        private String trailText;

        @JsonProperty("web_url")
        private String webUrl;

        private float similarity;

        @JsonProperty("korean_summary")
        private String koreanSummary;
    }

}
