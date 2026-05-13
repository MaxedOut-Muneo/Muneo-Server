package com.example.muneoserver.global.security.jwt;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.User;
import com.example.muneoserver.domain.user.domain.UserRole;
import com.example.muneoserver.global.security.auth.AuthUser;
import com.example.muneoserver.global.security.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecurityProperties.Jwt properties;
    private final SecretKey secretKey;

    public JwtTokenProvider(SecurityProperties securityProperties) {
        this.properties = securityProperties.jwt();
        this.secretKey = createSecretKey(properties.secret());
    }

    public String createAccessToken(User user) {
        return buildToken(user, properties.accessTokenExpiration());
    }

    public String createRefreshToken(User user) {
        return buildToken(user, properties.refreshTokenExpiration());
    }

    public long getRefreshTokenExpiration() {
        return properties.refreshTokenExpiration();
    }

    public long getAccessTokenExpiration() {
        return properties.accessTokenExpiration();
    }

    public AuthUser parseAuthUser(String token) {
        Claims claims = parseClaims(token);
        return new AuthUser(
                Long.valueOf(claims.getSubject()),
                claims.get("email", String.class),
                AuthProvider.valueOf(claims.get("authProvider", String.class)),
                Boolean.TRUE.equals(claims.get("profileCompleted", Boolean.class)),
                UserRole.valueOf(claims.get("role", String.class))
        );
    }

    public boolean isValidToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            log.debug("JWT validation failed", e);
            return false;
        }
    }

    private String buildToken(User user, long expirationSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail() == null ? "" : user.getEmail())
                .claim("authProvider", user.getAuthProvider().name())
                .claim("profileCompleted", user.isProfileCompleted())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(secretKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey createSecretKey(String secret) {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (Exception ignored) {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }
}
