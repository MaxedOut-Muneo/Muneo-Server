package com.example.muneoserver.domain.user.service.account;

import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.domain.user.dto.email.EmailCodeSendRequest;
import com.example.muneoserver.domain.user.dto.email.EmailCodeVerifyRequest;
import com.example.muneoserver.domain.user.dto.password.PasswordResetRequest;
import com.example.muneoserver.domain.user.dto.profile.LocalProfileUpdateRequest;
import com.example.muneoserver.domain.user.dto.profile.SocialProfileUpdateRequest;
import com.example.muneoserver.global.security.auth.AuthUser;

public interface UserAccountService {

    UserResponse updateLocalProfile(AuthUser authUser, LocalProfileUpdateRequest request);

    UserResponse updateSocialProfile(AuthUser authUser, SocialProfileUpdateRequest request);

    void withdraw(AuthUser authUser);

    void sendEmailVerificationCode(EmailCodeSendRequest request);

    void verifyEmailCode(EmailCodeVerifyRequest request);

    void sendPasswordResetCode(EmailCodeSendRequest request);

    void resetPassword(PasswordResetRequest request);
}
