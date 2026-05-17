package com.example.muneoserver.global.security.jwt;

import com.example.muneoserver.global.security.auth.AuthUser;
import com.example.muneoserver.global.security.cookie.AuthCookieManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthCookieManager authCookieManager;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthCookieManager authCookieManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authCookieManager = authCookieManager;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        return path.startsWith("/api/v1/users/signup")
                || path.startsWith("/api/v1/users/login")
                || path.startsWith("/api/v1/users/refresh")
                || path.startsWith("/api/v1/admin/login")
                || path.startsWith("/api/v1/auth")
                || path.startsWith("/oauth2/")
                || path.startsWith("/login/oauth2/")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/error");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> token = resolveTokenFromCookie(request, authCookieManager.getAccessTokenCookieName());

        if (token.isPresent() && jwtTokenProvider.isValidToken(token.get())) {
            AuthUser authUser = jwtTokenProvider.parseAuthUser(token.get());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    authUser,
                    null,
                    java.util.List.of(new SimpleGrantedAuthority("ROLE_" + authUser.role().name()))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
