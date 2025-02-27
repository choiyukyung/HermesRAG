package com.kayla.HermesRAG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VectorDTO {
    private String id;
    private byte[] webTitleEmbedding;
    private byte[] trailTextEmbedding;
}
