package com.example.muneoserver.domain.user.controller;

import com.example.muneoserver.domain.user.controller.docs.AdminUserControllerDocs;
import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.UserRole;
import com.example.muneoserver.domain.user.dto.admin.AdminUserResponse;
import com.example.muneoserver.domain.user.dto.admin.AdminUserRoleUpdateRequest;
import com.example.muneoserver.domain.user.dto.admin.AdminUserSearchCondition;
import com.example.muneoserver.domain.user.dto.admin.AdminUserUpdateRequest;
import com.example.muneoserver.domain.user.service.admin.AdminUserService;
import com.example.muneoserver.global.dto.ApiResponse;
import com.example.muneoserver.global.dto.PageResponse;
import com.example.muneoserver.global.security.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController implements AdminUserControllerDocs {

    private final AdminUserService adminUserService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AdminUserResponse>>> getUsers(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(required = false) AuthProvider authProvider,
            @RequestParam(required = false) Boolean emailVerified,
            @RequestParam(required = false) Boolean profileCompleted
    ) {
        AdminUserSearchCondition condition = new AdminUserSearchCondition(
                keyword,
                role,
                deleted,
                authProvider,
                emailVerified,
                profileCompleted
        );
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return ResponseEntity.ok(ApiResponse.success(adminUserService.getUsers(authUser, condition, pageRequest), "사용자 목록 조회에 성공했습니다."));
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.getUser(authUser, userId), "사용자 상세 조회에 성공했습니다."));
    }

    @Override
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userId,
            @RequestBody AdminUserUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.updateUser(authUser, userId, request), "사용자 정보 수정에 성공했습니다."));
    }

    @Override
    @PatchMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<AdminUserResponse>> updateUserRole(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userId,
            @RequestBody AdminUserRoleUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.updateUserRole(authUser, userId, request), "사용자 권한 변경에 성공했습니다."));
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userId
    ) {
        adminUserService.deleteUser(authUser, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "사용자 탈퇴 처리에 성공했습니다."));
    }

    @Override
    @PatchMapping("/{userId}/restore")
    public ResponseEntity<ApiResponse<AdminUserResponse>> restoreUser(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.restoreUser(authUser, userId), "사용자 복구에 성공했습니다."));
    }
}
