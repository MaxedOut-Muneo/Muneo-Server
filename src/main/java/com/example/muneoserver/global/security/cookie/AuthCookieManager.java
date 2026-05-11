package com.example.muneoserver.global.security.cookie;

import com.example.muneoserver.global.security.config.SecurityProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class AuthCookieManager {

    private final SecurityProperties.Cookie cookieProperties;

    public AuthCookieManager(SecurityProperties securityProperties) {
        this.cookieProperties = securityProperties.cookie();
    }

    public void addAccessTokenCookie(HttpServletResponse response, String accessToken, long maxAgeSeconds) {
        response.addHeader(HttpHeaders.SET_COOKIE, createCookie(cookieProperties.accessTokenName(), accessToken, maxAgeSeconds));
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAgeSeconds) {
        response.addHeader(HttpHeaders.SET_COOKIE, createCookie(cookieProperties.refreshTokenName(), refreshToken, maxAgeSeconds));
    }

    public void clearAuthCookies(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, createCookie(cookieProperties.accessTokenName(), "", 0));
        response.addHeader(HttpHeaders.SET_COOKIE, createCookie(cookieProperties.refreshTokenName(), "", 0));
    }

    public String getAccessTokenCookieName() {
        return cookieProperties.accessTokenName();
    }

    public String getRefreshTokenCookieName() {
        return cookieProperties.refreshTokenName();
    }

    private String createCookie(String name, String value, long maxAgeSeconds) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieProperties.secure())
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite(cookieProperties.sameSite());

        if (cookieProperties.domain() != null && !cookieProperties.domain().isBlank()) {
            builder.domain(cookieProperties.domain());
        }

        return builder.build().toString();
    }
}
