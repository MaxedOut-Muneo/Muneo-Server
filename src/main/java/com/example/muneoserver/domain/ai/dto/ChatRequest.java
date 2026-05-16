package com.example.muneoserver.domain.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "question은 필수입니다.")
        String question
) {
}
