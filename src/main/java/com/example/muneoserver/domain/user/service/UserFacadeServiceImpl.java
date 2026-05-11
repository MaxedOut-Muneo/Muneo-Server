package com.example.muneoserver.domain.user.service;

import com.example.muneoserver.domain.user.dto.LoginRequest;
import com.example.muneoserver.domain.user.dto.SignUpRequest;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.domain.user.service.command.UserCommandService;
import com.example.muneoserver.domain.user.service.command.dto.AuthResult;
import com.example.muneoserver.domain.user.service.command.dto.LoginCommand;
import com.example.muneoserver.domain.user.service.command.dto.SignUpCommand;
import com.example.muneoserver.domain.user.service.query.UserQueryService;
import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import com.example.muneoserver.global.security.auth.AuthUser;
import com.example.muneoserver.global.security.cookie.AuthCookieManager;
import com.example.muneoserver.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacadeServiceImpl implements UserFacadeService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final AuthCookieManager authCookieManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponse signUp(SignUpRequest request, HttpServletResponse response) {
        validatePasswordConfirm(request.password(), request.passwordConfirm());

        AuthResult authResult = userCommandService.signUp(
                new SignUpCommand(
                        request.email(),
                        request.password(),
                        request.name(),
                        request.phoneNumber(),
                        request.birthDate()
                )
        );

        setAuthCookies(response, authResult);
        return UserResponse.from(authResult.user());
    }

    @Override
    public UserResponse login(LoginRequest request, HttpServletResponse response) {
        AuthResult authResult = userCommandService.login(
                new LoginCommand(request.email(), request.password())
        );

        setAuthCookies(response, authResult);
        return UserResponse.from(authResult.user());
    }

    @Override
    public UserResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = resolveCookie(request, authCookieManager.getRefreshTokenCookieName())
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_TOKEN, "리프레시 토큰 쿠키가 없습니다."));

        AuthResult authResult = userCommandService.refresh(refreshToken);
        setAuthCookies(response, authResult);
        return UserResponse.from(authResult.user());
    }

    @Override
    public void logout(AuthUser authUser, HttpServletResponse response) {
        validateAuthenticated(authUser);
        userCommandService.logout(authUser.id());
        authCookieManager.clearAuthCookies(response);
    }

    @Override
    public UserResponse me(AuthUser authUser) {
        validateAuthenticated(authUser);
        return userQueryService.getMyInfo(authUser.id());
    }

    private void validatePasswordConfirm(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new CommonException(
                    ErrorCode.VALIDATION_FAILED,
                    Map.of("passwordConfirm", "비밀번호가 일치하지 않습니다.")
            );
        }
    }

    private void setAuthCookies(HttpServletResponse response, AuthResult authResult) {
        authCookieManager.addAccessTokenCookie(response, authResult.accessToken(), jwtTokenProvider.getAccessTokenExpiration());
        authCookieManager.addRefreshTokenCookie(response, authResult.refreshToken(), jwtTokenProvider.getRefreshTokenExpiration());
    }

    private Optional<String> resolveCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void validateAuthenticated(AuthUser authUser) {
        if (authUser == null) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }
    }
}
