package com.example.muneoserver.domain.user.service;

import com.example.muneoserver.domain.user.dto.LoginRequest;
import com.example.muneoserver.domain.user.dto.SignUpRequest;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.domain.user.dto.email.EmailCodeSendRequest;
import com.example.muneoserver.domain.user.dto.email.EmailCodeVerifyRequest;
import com.example.muneoserver.domain.user.dto.oauth.OAuthLoginUrlResponse;
import com.example.muneoserver.domain.user.dto.password.PasswordResetRequest;
import com.example.muneoserver.domain.user.dto.profile.LocalProfileUpdateRequest;
import com.example.muneoserver.domain.user.dto.profile.SocialProfileUpdateRequest;
import com.example.muneoserver.domain.user.dto.social.SocialSignUpRequest;
import com.example.muneoserver.global.security.auth.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserFacadeService {

    UserResponse signUp(SignUpRequest request, HttpServletResponse response);

    UserResponse login(LoginRequest request, HttpServletResponse response);

    UserResponse adminLogin(LoginRequest request, HttpServletResponse response);

    UserResponse refresh(HttpServletRequest request, HttpServletResponse response);

    void logout(AuthUser authUser, HttpServletResponse response);

    UserResponse me(AuthUser authUser);

    OAuthLoginUrlResponse kakaoLoginUrl();

    UserResponse completeSocialSignUp(SocialSignUpRequest request, HttpServletResponse response);

    UserResponse updateLocalProfile(AuthUser authUser, LocalProfileUpdateRequest request);

    UserResponse updateSocialProfile(AuthUser authUser, SocialProfileUpdateRequest request);

    void withdraw(AuthUser authUser, HttpServletResponse response);

    void sendEmailVerificationCode(EmailCodeSendRequest request);

    void verifyEmailCode(EmailCodeVerifyRequest request);

    void sendPasswordResetCode(EmailCodeSendRequest request);

    void resetPassword(PasswordResetRequest request);
}
