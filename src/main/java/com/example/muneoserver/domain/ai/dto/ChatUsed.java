package com.example.muneoserver.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatUsed(
        @JsonProperty("is_interior") boolean isInterior,
        @JsonProperty("use_estimate_cases") boolean useEstimateCases,
        @JsonProperty("use_legal_docs") boolean useLegalDocs,
        @JsonProperty("use_defect_docs") boolean useDefectDocs
) {
}
