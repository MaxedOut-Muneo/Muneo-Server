package com.example.muneoserver.global.security.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.muneoserver.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

class CustomAccessDeniedHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void handleWritesAdminAccessForbiddenResponse() throws Exception {
        CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.handle(new MockHttpServletRequest(), response, new AccessDeniedException("Forbidden"));

        JsonNode body = objectMapper.readTree(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(ErrorCode.ADMIN_ACCESS_FORBIDDEN.status());
        assertThat(body.get("success").asBoolean()).isFalse();
        assertThat(body.get("status").asInt()).isEqualTo(ErrorCode.ADMIN_ACCESS_FORBIDDEN.status());
        assertThat(body.get("code").asText()).isEqualTo(ErrorCode.ADMIN_ACCESS_FORBIDDEN.code());
        assertThat(body.get("message").asText()).isEqualTo(ErrorCode.ADMIN_ACCESS_FORBIDDEN.message());
    }
}
