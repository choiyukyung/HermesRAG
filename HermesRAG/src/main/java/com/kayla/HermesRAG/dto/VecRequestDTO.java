package com.kayla.HermesRAG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VecRequestDTO {
    private String id;
    private String webTitle;
    private String trailText;
    private String webUrl;
    private LocalDateTime webPublicationDate;
}
