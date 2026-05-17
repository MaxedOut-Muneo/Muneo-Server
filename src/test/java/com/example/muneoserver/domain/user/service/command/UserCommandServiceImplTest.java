package com.example.muneoserver.domain.user.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.User;
import com.example.muneoserver.domain.user.domain.UserRole;
import com.example.muneoserver.domain.user.repository.UserRepository;
import com.example.muneoserver.domain.user.service.command.dto.AuthResult;
import com.example.muneoserver.domain.user.service.command.dto.LoginCommand;
import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import com.example.muneoserver.global.security.jwt.JwtTokenProvider;
import com.example.muneoserver.global.security.redis.RefreshTokenStore;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenStore refreshTokenStore;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @Test
    void adminLoginRejectsNonAdminUser() {
        User user = User.create("user@example.com", "encoded-password", "사용자", "01012345678", LocalDate.of(2000, 1, 1));
        LoginCommand command = new LoginCommand("user@example.com", "password");

        when(userRepository.findByEmailAndAuthProvider(command.email(), AuthProvider.LOCAL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(command.password(), user.getPassword())).thenReturn(true);

        assertThatThrownBy(() -> userCommandService.adminLogin(command))
                .isInstanceOf(CommonException.class)
                .satisfies(exception -> {
                    CommonException commonException = (CommonException) exception;
                    assertThat(commonException.getErrorCode()).isEqualTo(ErrorCode.ADMIN_LOGIN_FORBIDDEN);
                });

        verify(jwtTokenProvider, never()).createAccessToken(any());
        verify(jwtTokenProvider, never()).createRefreshToken(any());
        verify(refreshTokenStore, never()).save(any(), any(), anyLong());
    }

    @Test
    void adminLoginIssuesTokensForAdminUser() {
        User admin = User.create("admin@example.com", "encoded-password", "관리자", "01012345678", LocalDate.of(2000, 1, 1));
        ReflectionTestUtils.setField(admin, "id", 1L);
        ReflectionTestUtils.setField(admin, "role", UserRole.ADMIN);
        LoginCommand command = new LoginCommand("admin@example.com", "password");

        when(userRepository.findByEmailAndAuthProvider(command.email(), AuthProvider.LOCAL)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(command.password(), admin.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(admin)).thenReturn("access-token");
        when(jwtTokenProvider.createRefreshToken(admin)).thenReturn("refresh-token");
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(3600L);

        AuthResult result = userCommandService.adminLogin(command);

        assertThat(result.user()).isEqualTo(admin);
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");
        verify(refreshTokenStore).save(1L, "refresh-token", 3600L);
    }
}
