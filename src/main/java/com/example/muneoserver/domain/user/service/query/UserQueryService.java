package com.example.muneoserver.domain.user.service.query;

import com.example.muneoserver.domain.user.dto.UserResponse;

public interface UserQueryService {

    UserResponse getMyInfo(Long userId);
}
