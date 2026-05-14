package com.example.muneoserver.domain.user.service.oauth;

import com.example.muneoserver.domain.user.dto.social.SocialSignUpRequest;
import com.example.muneoserver.domain.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface OAuth2LoginService {

    String getKakaoLoginUrl();

    String handleSuccess(String registrationId, Map<String, Object> attributes, HttpServletResponse response);

    String handleFailureRedirect(String reason);

    UserResponse completeSocialSignUp(SocialSignUpRequest request, HttpServletResponse response);
}
