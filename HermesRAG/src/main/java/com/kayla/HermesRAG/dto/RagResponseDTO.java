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
    private List<RetrievedArticle> articles;

    @JsonProperty("english_summary")
    private String englishSummary;

    @Getter
    @Setter
    public static class RetrievedArticle {
        private String id;

        @JsonProperty("web_title")
        private String webTitle;

        @JsonProperty("trail_text")
        private String trailText;

        private float similarity;
    }

}
