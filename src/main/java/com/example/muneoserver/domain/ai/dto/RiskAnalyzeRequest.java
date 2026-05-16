package com.example.muneoserver.domain.ai.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record RiskAnalyzeRequest(
        @NotBlank(message = "space_type은 필수입니다.")
        String spaceType,

        @NotNull(message = "pyeong은 필수입니다.")
        @Min(value = 1, message = "pyeong은 1 이상이어야 합니다.")
        Integer pyeong,

        @NotNull(message = "room_count는 필수입니다.")
        @Min(value = 0, message = "room_count는 0 이상이어야 합니다.")
        Integer roomCount,

        @NotNull(message = "floor는 필수입니다.")
        Integer floor,

        @NotNull(message = "elevator는 필수입니다.")
        Boolean elevator,

        @NotBlank(message = "region은 필수입니다.")
        String region,

        @NotBlank(message = "building_age는 필수입니다.")
        String buildingAge,

        @NotBlank(message = "company_name은 필수입니다.")
        String companyName,

        @NotEmpty(message = "files는 최소 1개 이상이어야 합니다.")
        MultipartFile[] files
) {
}
