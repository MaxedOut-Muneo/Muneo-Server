package com.example.muneoserver.domain.user.dto;

import com.example.muneoserver.domain.user.domain.User;
import com.example.muneoserver.domain.user.domain.UserRole;
import java.time.LocalDate;

public record UserResponse(
        Long id,
        String email,
        String name,
        String phoneNumber,
        LocalDate birthDate,
        UserRole role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getRole()
        );
    }
}
