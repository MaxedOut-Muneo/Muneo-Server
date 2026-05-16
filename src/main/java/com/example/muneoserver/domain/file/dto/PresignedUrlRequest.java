package com.example.muneoserver.domain.file.dto;

public record PresignedUrlRequest(
        String type,
        String filename,
        String contentType
) {
}
