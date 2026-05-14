package com.example.muneoserver.global.security.oauth;

import com.example.muneoserver.domain.user.service.oauth.OAuth2LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2LoginService oAuth2LoginService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            response.sendRedirect(oAuth2LoginService.handleFailureRedirect("invalid_authentication"));
            return;
        }

        OAuth2User oAuth2User = token.getPrincipal();
        String redirectUri = oAuth2LoginService.handleSuccess(
                token.getAuthorizedClientRegistrationId(),
                oAuth2User.getAttributes(),
                response
        );
        response.sendRedirect(redirectUri);
    }
}
