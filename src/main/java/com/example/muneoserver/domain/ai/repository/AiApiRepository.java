package com.example.muneoserver.domain.ai.repository;

import com.example.muneoserver.domain.ai.dto.ChatRequest;
import com.example.muneoserver.domain.ai.dto.EstimateGenerateRequest;
import com.example.muneoserver.domain.ai.dto.EstimateSaveRequest;
import com.example.muneoserver.domain.ai.dto.RiskAnalyzeRequest;
import org.springframework.http.ResponseEntity;

public interface AiApiRepository {

    ResponseEntity<Object> generateEstimate(EstimateGenerateRequest request);

    ResponseEntity<Object> saveEstimate(String userId, EstimateSaveRequest request);

    ResponseEntity<Object> getEstimates(String userId);

    ResponseEntity<Object> deleteEstimate(String userId, String estimateId);

    ResponseEntity<Object> chat(ChatRequest request);

    ResponseEntity<Object> analyzeRisk(RiskAnalyzeRequest request);
}
