package com.example.muneoserver.global.security.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.muneoserver.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;

class CustomAuthenticationEntryPointTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void commenceWritesUnauthorizedResponse() throws Exception {
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(
                new MockHttpServletRequest(),
                response,
                new InsufficientAuthenticationException("Authentication required")
        );

        JsonNode body = objectMapper.readTree(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(ErrorCode.UNAUTHORIZED.status());
        assertThat(body.get("success").asBoolean()).isFalse();
        assertThat(body.get("status").asInt()).isEqualTo(ErrorCode.UNAUTHORIZED.status());
        assertThat(body.get("code").asText()).isEqualTo(ErrorCode.UNAUTHORIZED.code());
        assertThat(body.get("message").asText()).isEqualTo(ErrorCode.UNAUTHORIZED.message());
    }
}
