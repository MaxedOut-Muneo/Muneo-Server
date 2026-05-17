package com.example.muneoserver.domain.user.dto.admin;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.UserRole;

public record AdminUserSearchCondition(
        String keyword,
        UserRole role,
        Boolean deleted,
        AuthProvider authProvider,
        Boolean emailVerified,
        Boolean profileCompleted
) {
}
