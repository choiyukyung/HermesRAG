package com.kayla.HermesRAG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchRequestDTO {
    private String id;
    private String webTitle;
    private String trailText;
    private String webUrl;
    private byte[] webTitleEmbedding;
    private byte[] trailTextEmbedding;
}
