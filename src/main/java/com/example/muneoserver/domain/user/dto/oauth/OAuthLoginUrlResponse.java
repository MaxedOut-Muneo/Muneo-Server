package com.example.muneoserver.domain.user.dto.oauth;

public record OAuthLoginUrlResponse(
        String provider,
        String loginUrl
) {
}
