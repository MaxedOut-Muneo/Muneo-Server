package com.example.muneoserver.domain.user.service;

import com.example.muneoserver.domain.user.dto.LoginRequest;
import com.example.muneoserver.domain.user.dto.SignUpRequest;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.global.security.auth.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserFacadeService {

    UserResponse signUp(SignUpRequest request, HttpServletResponse response);

    UserResponse login(LoginRequest request, HttpServletResponse response);

    UserResponse refresh(HttpServletRequest request, HttpServletResponse response);

    void logout(AuthUser authUser, HttpServletResponse response);

    UserResponse me(AuthUser authUser);
}
