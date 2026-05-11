package com.example.muneoserver.domain.user.service.command;

import com.example.muneoserver.domain.user.service.command.dto.AuthResult;
import com.example.muneoserver.domain.user.service.command.dto.LoginCommand;
import com.example.muneoserver.domain.user.service.command.dto.SignUpCommand;

public interface UserCommandService {

    AuthResult signUp(SignUpCommand command);

    AuthResult login(LoginCommand command);

    AuthResult refresh(String refreshToken);

    void logout(Long userId);
}
