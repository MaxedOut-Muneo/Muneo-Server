package com.example.muneoserver.domain.user.dto.admin;

import com.example.muneoserver.domain.user.domain.UserRole;
import jakarta.validation.constraints.NotNull;

public record AdminUserRoleUpdateRequest(
        @NotNull(message = "권한은 필수입니다.")
        UserRole role
) {
}
