package com.example.muneoserver.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record EstimateItemResponse(
        String id,
        @JsonProperty("user_id") String userId,
        @JsonProperty("created_at") String createdAt,
        Map<String, Object> input,
        Map<String, Object> result
) {
}
