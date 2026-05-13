package com.example.muneoserver.domain.user.controller;

import com.example.muneoserver.domain.user.dto.LoginRequest;
import com.example.muneoserver.domain.user.dto.SignUpRequest;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.domain.user.dto.profile.LocalProfileUpdateRequest;
import com.example.muneoserver.domain.user.dto.profile.SocialProfileUpdateRequest;
import com.example.muneoserver.domain.user.service.UserFacadeService;
import com.example.muneoserver.global.dto.ApiResponse;
import com.example.muneoserver.global.security.auth.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacadeService userFacadeService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(
            @Valid @RequestBody SignUpRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(userFacadeService.signUp(request, response), "회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(ApiResponse.success(userFacadeService.login(request, response), "로그인에 성공했습니다."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<UserResponse>> refresh(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success(userFacadeService.refresh(request, response), "토큰이 재발급되었습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal AuthUser authUser,
            HttpServletResponse response
    ) {
        userFacadeService.logout(authUser, response);
        return ResponseEntity.ok(ApiResponse.success(null, "로그아웃이 완료되었습니다."));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(ApiResponse.success(userFacadeService.me(authUser), "내 정보 조회에 성공했습니다."));
    }

    @PatchMapping("/me/local")
    public ResponseEntity<ApiResponse<UserResponse>> updateLocalProfile(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody LocalProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(userFacadeService.updateLocalProfile(authUser, request), "일반 회원 정보 수정에 성공했습니다."));
    }

    @PatchMapping("/me/social")
    public ResponseEntity<ApiResponse<UserResponse>> updateSocialProfile(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody SocialProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(userFacadeService.updateSocialProfile(authUser, request), "소셜 회원 정보 수정에 성공했습니다."));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal AuthUser authUser,
            HttpServletResponse response
    ) {
        userFacadeService.withdraw(authUser, response);
        return ResponseEntity.ok(ApiResponse.success(null, "회원 탈퇴가 완료되었습니다."));
    }
}
