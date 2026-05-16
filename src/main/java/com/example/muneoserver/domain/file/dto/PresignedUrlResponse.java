package com.example.muneoserver.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record PresignedUrlResponse(
        @Schema(description = "업로드 타입", example = "Post")
        String type,
        @Schema(description = "S3에 저장될 object key", example = "post/1/550e8400-e29b-41d4-a716-446655440000.jpg")
        String key,
        @Schema(description = "프론트엔드가 PUT 요청으로 업로드할 presigned URL")
        String uploadUrl,
        @Schema(description = "업로드 완료 후 접근 가능한 S3 파일 URL")
        String fileUrl,
        @Schema(description = "업로드 시 사용할 HTTP 메서드", example = "PUT")
        String uploadMethod,
        @Schema(description = "업로드 시 반드시 동일하게 전송해야 하는 Content-Type", example = "image/jpeg")
        String contentType,
        @Schema(description = "Presigned URL 만료 시각(UTC)")
        Instant expiresAt
) {
}
