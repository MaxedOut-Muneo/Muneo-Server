package com.example.muneoserver.domain.user.dto.admin;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.User;
import com.example.muneoserver.domain.user.domain.UserRole;
import java.time.LocalDate;

public record AdminUserResponse(
        Long id,
        String email,
        String name,
        String phoneNumber,
        LocalDate birthDate,
        AuthProvider authProvider,
        boolean profileCompleted,
        boolean emailVerified,
        boolean deleted,
        UserRole role
) {

    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getAuthProvider(),
                user.isProfileCompleted(),
                user.isEmailVerified(),
                user.isDeleted(),
                user.getRole()
        );
    }
}
