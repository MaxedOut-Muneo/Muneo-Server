package com.example.muneoserver.domain.user.controller;

import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.domain.user.dto.email.EmailCodeSendRequest;
import com.example.muneoserver.domain.user.dto.email.EmailCodeVerifyRequest;
import com.example.muneoserver.domain.user.dto.oauth.OAuthLoginUrlResponse;
import com.example.muneoserver.domain.user.dto.password.PasswordResetRequest;
import com.example.muneoserver.domain.user.dto.social.SocialSignUpRequest;
import com.example.muneoserver.domain.user.service.UserFacadeService;
import com.example.muneoserver.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserFacadeService userFacadeService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<ApiResponse<OAuthLoginUrlResponse>> kakaoLoginUrl() {
        return ResponseEntity.ok(ApiResponse.success(userFacadeService.kakaoLoginUrl(), "카카오 로그인 URL 조회에 성공했습니다."));
    }

    @PostMapping("/social/signup")
    public ResponseEntity<ApiResponse<UserResponse>> completeSocialSignUp(
            @Valid @RequestBody SocialSignUpRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(userFacadeService.completeSocialSignUp(request, response), "소셜 회원가입이 완료되었습니다."));
    }

    @PostMapping("/email/verification/request")
    public ResponseEntity<ApiResponse<Void>> requestEmailVerificationCode(@Valid @RequestBody EmailCodeSendRequest request) {
        userFacadeService.sendEmailVerificationCode(request);
        return ResponseEntity.ok(ApiResponse.success(null, "이메일 인증 코드를 발송했습니다."));
    }

    @PostMapping("/email/verification/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmEmailVerificationCode(@Valid @RequestBody EmailCodeVerifyRequest request) {
        userFacadeService.verifyEmailCode(request);
        return ResponseEntity.ok(ApiResponse.success(null, "이메일 인증이 완료되었습니다."));
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity<ApiResponse<Void>> requestPasswordResetCode(@Valid @RequestBody EmailCodeSendRequest request) {
        userFacadeService.sendPasswordResetCode(request);
        return ResponseEntity.ok(ApiResponse.success(null, "비밀번호 재설정 코드를 발송했습니다."));
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        userFacadeService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "비밀번호 재설정이 완료되었습니다."));
    }
}
