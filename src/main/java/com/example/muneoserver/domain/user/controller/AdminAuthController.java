package com.example.muneoserver.domain.user.controller;

import com.example.muneoserver.domain.user.controller.docs.AdminAuthControllerDocs;
import com.example.muneoserver.domain.user.dto.LoginRequest;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.domain.user.service.UserFacadeService;
import com.example.muneoserver.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminAuthController implements AdminAuthControllerDocs {

    private final UserFacadeService userFacadeService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(ApiResponse.success(userFacadeService.adminLogin(request, response), "관리자 로그인에 성공했습니다."));
    }
}
