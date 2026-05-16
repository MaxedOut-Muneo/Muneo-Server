package com.example.muneoserver.domain.ai.dto;

import java.util.List;

public record ChatResponse(
        String answer,
        ChatUsed used,
        List<ChatSource> sources
) {
}
