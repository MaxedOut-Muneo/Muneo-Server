package com.example.muneoserver.domain.ai.dto;

import java.util.Map;

public record RiskAnalyzeResponse(
        Map<String, Object> report
) {
}
