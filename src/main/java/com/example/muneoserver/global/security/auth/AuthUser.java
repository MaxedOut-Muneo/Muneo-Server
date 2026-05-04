package com.example.muneoserver.global.security.auth;

import com.example.muneoserver.domain.user.domain.UserRole;

public record AuthUser(
        Long id,
        String email,
        UserRole role
) {
}
