package com.example.muneoserver.global.config.s3;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.s3")
public record S3Properties(
        String bucket,
        String region,
        Duration presignedUrlExpiration,
        String estimateDirectory,
        String postDirectory
) {

    public S3Properties {
        bucket = normalize(bucket);
        region = normalize(region);
        estimateDirectory = normalize(estimateDirectory);
        postDirectory = normalize(postDirectory);
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
