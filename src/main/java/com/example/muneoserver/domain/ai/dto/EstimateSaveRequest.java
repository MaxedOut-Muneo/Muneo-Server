package com.example.muneoserver.domain.ai.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record EstimateSaveRequest(
        @NotNull(message = "input은 필수입니다.")
        Map<String, Object> input,

        @NotNull(message = "result는 필수입니다.")
        Map<String, Object> result
) {
}
