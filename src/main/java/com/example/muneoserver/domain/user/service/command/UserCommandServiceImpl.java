package com.example.muneoserver.domain.user.service.command;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.User;
import com.example.muneoserver.domain.user.domain.UserRole;
import com.example.muneoserver.domain.user.repository.UserRepository;
import com.example.muneoserver.domain.user.service.command.dto.AuthResult;
import com.example.muneoserver.domain.user.service.command.dto.LoginCommand;
import com.example.muneoserver.domain.user.service.command.dto.SignUpCommand;
import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import com.example.muneoserver.global.security.auth.AuthUser;
import com.example.muneoserver.global.security.jwt.JwtTokenProvider;
import com.example.muneoserver.global.security.redis.RefreshTokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStore refreshTokenStore;

    @Override
    public AuthResult signUp(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new CommonException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }

        User user = userRepository.save(
                User.create(
                        command.email(),
                        passwordEncoder.encode(command.password()),
                        command.name(),
                        command.phoneNumber(),
                        command.birthDate()
                )
        );

        return issueTokens(user);
    }

    @Override
    public AuthResult login(LoginCommand command) {
        User user = authenticateLocalUser(command);
        return issueTokens(user);
    }

    @Override
    public AuthResult adminLogin(LoginCommand command) {
        User user = authenticateLocalUser(command);

        if (user.getRole() != UserRole.ADMIN) {
            throw new CommonException(ErrorCode.ADMIN_LOGIN_FORBIDDEN);
        }

        return issueTokens(user);
    }

    @Override
    public AuthResult refresh(String refreshToken) {
        if (!jwtTokenProvider.isValidToken(refreshToken)) {
            throw new CommonException(ErrorCode.INVALID_TOKEN);
        }

        AuthUser authUser = jwtTokenProvider.parseAuthUser(refreshToken);
        String savedToken = refreshTokenStore.find(authUser.id())
                .orElseThrow(() -> new CommonException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!savedToken.equals(refreshToken)) {
            throw new CommonException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        User user = userRepository.findById(authUser.id())
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        return issueTokens(user);
    }

    @Override
    public void logout(Long userId) {
        refreshTokenStore.delete(userId);
    }

    private User authenticateLocalUser(LoginCommand command) {
        User user = userRepository.findByEmailAndAuthProvider(command.email(), AuthProvider.LOCAL)
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_LOGIN_INFO));

        if (user.isDeleted()) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new CommonException(ErrorCode.INVALID_LOGIN_INFO);
        }

        return user;
    }

    private AuthResult issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenStore.save(user.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return new AuthResult(user, accessToken, refreshToken);
    }
}
