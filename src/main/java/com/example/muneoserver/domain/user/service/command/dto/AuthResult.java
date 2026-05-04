package com.example.muneoserver.domain.user.service.command.dto;

import com.example.muneoserver.domain.user.domain.User;

public record AuthResult(
        User user,
        String accessToken,
        String refreshToken
) {
}
