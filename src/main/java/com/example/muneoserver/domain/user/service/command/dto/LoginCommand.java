package com.example.muneoserver.domain.user.service.command.dto;

public record LoginCommand(
        String email,
        String password
) {
}
