package com.example.muneoserver.domain.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai")
public record AiProperties(
        String baseUrl,
        int connectTimeoutMs,
        int readTimeoutMs
) {
}
