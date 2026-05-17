package com.example.muneoserver.domain.user.controller.docs;

import com.example.muneoserver.domain.user.dto.LoginRequest;
import com.example.muneoserver.domain.user.dto.UserResponse;
import com.example.muneoserver.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Auth", description = "관리자 인증 관련 API")
public interface AdminAuthControllerDocs {

    @Operation(
            summary = "관리자 로그인",
            description = "이메일과 비밀번호로 로그인합니다. UserRole이 ADMIN인 로컬 계정만 로그인할 수 있으며, "
                    + "성공 시 HttpOnly Cookie(access_token, refresh_token)를 발급합니다."
    )
    @SecurityRequirements
    @RequestBody(
            required = true,
            description = "관리자 로그인 요청 정보",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = @ExampleObject(
                            name = "관리자 로그인 요청",
                            value = """
                                    {
                                      "email": "admin@example.com",
                                      "password": "password1234"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "관리자 로그인 성공",
                    headers = {
                            @Header(name = "Set-Cookie", description = "access_token HttpOnly Cookie"),
                            @Header(name = "Set-Cookie", description = "refresh_token HttpOnly Cookie")
                    },
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "관리자 로그인 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "code": "SUCCESS",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "관리자 로그인에 성공했습니다.",
                                              "result": {
                                                "id": 1,
                                                "email": "admin@example.com",
                                                "name": "관리자",
                                                "phoneNumber": "01012345678",
                                                "birthDate": "2000-01-01",
                                                "authProvider": "LOCAL",
                                                "profileCompleted": true,
                                                "role": "ADMIN"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 값 오류 또는 요청 JSON 형식 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "요청 값 오류",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "VALIDATION_FAILED",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "요청 값이 올바르지 않습니다.",
                                                      "error": {
                                                        "email": "올바른 이메일 형식이어야 합니다.",
                                                        "password": "비밀번호는 필수입니다."
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "요청 JSON 형식 오류",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "INVALID_REQUEST_FORMAT",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "올바르지 않은 요청 형식입니다."
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "이메일 또는 비밀번호 불일치",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "로그인 정보 불일치",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 401,
                                              "code": "INVALID_LOGIN_INFO",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "이메일 또는 비밀번호가 올바르지 않습니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "관리자 계정이 아님",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "관리자 로그인 권한 없음",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 403,
                                              "code": "ADMIN_LOGIN_FORBIDDEN",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "관리자 계정만 로그인할 수 있습니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "삭제된 사용자 계정",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 없음",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "code": "USER_NOT_FOUND",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "사용자를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 500,
                                              "code": "INTERNAL_SERVER_ERROR",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "서버 내부 에러"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponse<UserResponse>> login(
            LoginRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    );
}
