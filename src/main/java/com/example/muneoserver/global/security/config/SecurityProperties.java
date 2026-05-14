package com.example.muneoserver.global.security.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record SecurityProperties(
        Security security,
        Cors cors
) {

    public Jwt jwt() {
        return security.jwt();
    }

    public Cookie cookie() {
        return security.cookie();
    }

    public Oauth oauth() {
        return security.oauth();
    }

    public Email email() {
        return security.email();
    }

    public record Security(
            Jwt jwt,
            Cookie cookie,
            Oauth oauth,
            Email email
    ) {
    }

    public record Cors(List<String> allowedOrigins) {
    }

    public record Jwt(
            String secret,
            long accessTokenExpiration,
            long refreshTokenExpiration
    ) {
    }

    public record Cookie(
            String accessTokenName,
            String refreshTokenName,
            String sameSite,
            String domain,
            boolean secure
    ) {
    }

    public record Oauth(
            String frontendSuccessUri,
            String frontendSignupUri,
            String frontendFailureUri,
            long socialSignupTicketExpiration
    ) {
    }

    public record Email(
            boolean enabled,
            long verificationCodeExpiration,
            long passwordResetCodeExpiration
    ) {
    }
}
