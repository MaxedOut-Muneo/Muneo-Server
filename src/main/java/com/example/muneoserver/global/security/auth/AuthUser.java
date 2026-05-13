package com.example.muneoserver.global.security.auth;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.UserRole;

public record AuthUser(
        Long id,
        String email,
        AuthProvider authProvider,
        boolean profileCompleted,
        UserRole role
) {
}
