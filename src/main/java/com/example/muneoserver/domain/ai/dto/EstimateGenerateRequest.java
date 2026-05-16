package com.example.muneoserver.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EstimateGenerateRequest(
        @JsonProperty("공종")
        @NotEmpty(message = "공종은 필수입니다.")
        List<String> processTypes,

        @JsonProperty("평수")
        @NotNull(message = "평수는 필수입니다.")
        Integer pyeong,

        @JsonProperty("지역")
        @NotBlank(message = "지역은 필수입니다.")
        String region,

        @JsonProperty("시공범위")
        @NotBlank(message = "시공범위는 필수입니다.")
        String constructionScope,

        @JsonProperty("공간유형")
        @NotBlank(message = "공간유형은 필수입니다.")
        String spaceType,

        @JsonProperty("방개수")
        @NotNull(message = "방개수는 필수입니다.")
        Integer roomCount,

        @JsonProperty("건물연식")
        @NotBlank(message = "건물연식은 필수입니다.")
        String buildingAge,

        @JsonProperty("자재등급")
        @NotBlank(message = "자재등급은 필수입니다.")
        String materialGrade,

        @JsonProperty("철거여부")
        @NotBlank(message = "철거여부는 필수입니다.")
        String demolition,

        @JsonProperty("층수")
        @NotNull(message = "층수는 필수입니다.")
        Integer floor,

        @JsonProperty("엘리베이터")
        @NotBlank(message = "엘리베이터는 필수입니다.")
        String elevator,

        @JsonProperty("트럭접근")
        @NotBlank(message = "트럭접근은 필수입니다.")
        String truckAccess,

        @JsonProperty("거주중공사")
        @NotBlank(message = "거주중공사는 필수입니다.")
        String occupiedConstruction,

        @JsonProperty("공사시기")
        @NotBlank(message = "공사시기는 필수입니다.")
        String constructionTiming,

        @JsonProperty("도배")
        Map<String, Object> wallpaper,

        @JsonProperty("마루")
        Map<String, Object> floorMaterial,

        @JsonProperty("욕실")
        Map<String, Object> bathroom,

        @JsonProperty("주방")
        Map<String, Object> kitchen
) {
}
