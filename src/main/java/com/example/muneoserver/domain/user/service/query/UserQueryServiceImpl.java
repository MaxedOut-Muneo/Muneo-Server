package com.example.muneoserver.domain.user.service.query;

import com.example.muneoserver.domain.user.domain.User;
import com.example.muneoserver.domain.user.repository.UserRepository;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }
}
