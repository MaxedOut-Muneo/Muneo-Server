package com.example.muneoserver.domain.user.service.admin;

import com.example.muneoserver.domain.user.dto.admin.AdminUserResponse;
import com.example.muneoserver.domain.user.dto.admin.AdminUserRoleUpdateRequest;
import com.example.muneoserver.domain.user.dto.admin.AdminUserSearchCondition;
import com.example.muneoserver.domain.user.dto.admin.AdminUserUpdateRequest;
import com.example.muneoserver.global.dto.PageResponse;
import com.example.muneoserver.global.security.auth.AuthUser;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    PageResponse<AdminUserResponse> getUsers(AuthUser authUser, AdminUserSearchCondition condition, Pageable pageable);

    AdminUserResponse getUser(AuthUser authUser, Long userId);

    AdminUserResponse updateUser(AuthUser authUser, Long userId, AdminUserUpdateRequest request);

    AdminUserResponse updateUserRole(AuthUser authUser, Long userId, AdminUserRoleUpdateRequest request);

    void deleteUser(AuthUser authUser, Long userId);

    AdminUserResponse restoreUser(AuthUser authUser, Long userId);
}
