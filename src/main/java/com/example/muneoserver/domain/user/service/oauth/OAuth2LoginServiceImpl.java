package com.example.muneoserver.domain.user.service.oauth;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.User;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.domain.user.dto.social.SocialSignUpRequest;
import com.example.muneoserver.domain.user.repository.UserRepository;
import com.example.muneoserver.domain.user.service.command.dto.AuthResult;
import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import com.example.muneoserver.global.security.config.SecurityProperties;
import com.example.muneoserver.global.security.cookie.AuthCookieManager;
import com.example.muneoserver.global.security.jwt.JwtTokenProvider;
import com.example.muneoserver.global.security.redis.RefreshTokenStore;
import com.example.muneoserver.global.security.redis.social.SocialSignupTicketStore;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2LoginServiceImpl implements OAuth2LoginService {
    private static final String KAKAO_OAUTH_START_URL = "https://api.muneo.ai.kr/oauth2/authorization/kakao";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStore refreshTokenStore;
    private final AuthCookieManager authCookieManager;
    private final SocialSignupTicketStore socialSignupTicketStore;
    private final SecurityProperties securityProperties;

    @Override
    public String getKakaoLoginUrl() {
        return KAKAO_OAUTH_START_URL;
    }

    @Override
    public String handleSuccess(String registrationId, Map<String, Object> attributes, HttpServletResponse response) {
        if (!"kakao".equals(registrationId)) {
            throw new CommonException(ErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);
        }

        String providerId = String.valueOf(attributes.get("id"));

        User user = userRepository.findByAuthProviderAndProviderId(AuthProvider.KAKAO, providerId)
                .orElseGet(() -> userRepository.save(User.createSocialKakao(providerId, null, null, null)));

        if (user.isDeleted()) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        if (!user.isProfileCompleted()) {
            String ticket = UUID.randomUUID().toString();
            socialSignupTicketStore.save(
                    ticket,
                    user.getId(),
                    securityProperties.oauth().socialSignupTicketExpiration()
            );
            return withParam(securityProperties.oauth().frontendSignupUri(), "ticket", ticket);
        }

        AuthResult authResult = issueTokens(user);
        setAuthCookies(response, authResult);
        return securityProperties.oauth().frontendSuccessUri();
    }

    @Override
    public String handleFailureRedirect(String reason) {
        return withParam(securityProperties.oauth().frontendFailureUri(), "reason", reason == null ? "oauth_failed" : reason);
    }

    @Override
    public UserResponse completeSocialSignUp(SocialSignUpRequest request, HttpServletResponse response) {
        Long userId = socialSignupTicketStore.findUserId(request.ticket())
                .orElseThrow(() -> new CommonException(ErrorCode.SOCIAL_SIGNUP_TICKET_EXPIRED));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

        if (user.getAuthProvider() != AuthProvider.KAKAO) {
            throw new CommonException(ErrorCode.SOCIAL_PROVIDER_MISMATCH);
        }

        user.completeSocialSignup(request.name(), request.phoneNumber(), request.birthDate());
        socialSignupTicketStore.delete(request.ticket());

        AuthResult authResult = issueTokens(user);
        setAuthCookies(response, authResult);
        return UserResponse.from(user);
    }

    private AuthResult issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenStore.save(user.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());
        return new AuthResult(user, accessToken, refreshToken);
    }

    private void setAuthCookies(HttpServletResponse response, AuthResult authResult) {
        authCookieManager.addAccessTokenCookie(response, authResult.accessToken(), jwtTokenProvider.getAccessTokenExpiration());
        authCookieManager.addRefreshTokenCookie(response, authResult.refreshToken(), jwtTokenProvider.getRefreshTokenExpiration());
    }

    private String withParam(String baseUri, String key, String value) {
        String delimiter = baseUri.contains("?") ? "&" : "?";
        return baseUri + delimiter + key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
