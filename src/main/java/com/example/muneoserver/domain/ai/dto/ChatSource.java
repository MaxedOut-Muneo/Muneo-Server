package com.example.muneoserver.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatSource(
        String title,
        @JsonProperty("source_type") String sourceType,
        @JsonProperty("article_no") String articleNo,
        String section,
        @JsonProperty("defect_category") String defectCategory,
        Integer page,
        Double distance
) {
}
