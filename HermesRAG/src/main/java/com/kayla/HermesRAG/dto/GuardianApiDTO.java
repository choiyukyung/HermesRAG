package com.kayla.HermesRAG.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GuardianApiDTO {
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private int pages;
        private List<Result> results;
    }

    @Getter
    @Setter
    public static class Result {
        private String id;
        private String type;
        private String sectionId;
        private String sectionName;
        private LocalDateTime webPublicationDate;
        private String webTitle;
        private String webUrl;
        private String apiUrl;
        private boolean isHosted;
        private String pillarId;
        private String pillarName;

        private Fields fields;
    }

    @Getter
    @Setter
    public static class Fields {
        private String trailText;
    }
}
