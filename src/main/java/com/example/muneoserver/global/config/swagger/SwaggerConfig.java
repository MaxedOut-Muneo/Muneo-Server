package com.example.muneoserver.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Muneo Server API 명세서",
                description = "Muneo-Server 프로젝트의 REST API 문서입니다.",
                version = "v0.0.1"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local"),
                @Server(url = "https://dev.muneo.example.com", description = "Development"),
                @Server(url = "https://muneo.example.com", description = "Production")
        },
        security = {
                @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
        }
)
@SecurityScheme(
        name = SwaggerConfig.SECURITY_SCHEME_NAME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Access Token"
)
public class SwaggerConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";
}
