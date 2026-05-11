package com.example.muneoserver.domain.user.service.command.dto;

import java.time.LocalDate;

public record SignUpCommand(
        String email,
        String password,
        String name,
        String phoneNumber,
        LocalDate birthDate
) {
}
