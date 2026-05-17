package com.example.muneoserver.domain.user.controller.docs;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.UserRole;
import com.example.muneoserver.domain.user.dto.admin.AdminUserResponse;
import com.example.muneoserver.domain.user.dto.admin.AdminUserRoleUpdateRequest;
import com.example.muneoserver.domain.user.dto.admin.AdminUserUpdateRequest;
import com.example.muneoserver.global.dto.ApiResponse;
import com.example.muneoserver.global.dto.PageResponse;
import com.example.muneoserver.global.security.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Users", description = "관리자 사용자 관리 API")
public interface AdminUserControllerDocs {

    String USER_SUCCESS_EXAMPLE = """
            {
              "success": true,
              "status": 200,
              "code": "SUCCESS",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "사용자 상세 조회에 성공했습니다.",
              "result": {
                "id": 10,
                "email": "user@example.com",
                "name": "홍길동",
                "phoneNumber": "010-1234-5678",
                "birthDate": "2000-01-01",
                "authProvider": "LOCAL",
                "profileCompleted": true,
                "emailVerified": false,
                "deleted": false,
                "role": "USER"
              }
            }
            """;

    String INVALID_PARAMETER_EXAMPLE = """
            {
              "success": false,
              "status": 400,
              "code": "INVALID_PARAMETER_TYPE",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "적절하지 않은 파라미터 타입입니다."
            }
            """;

    String UNAUTHORIZED_EXAMPLE = """
            {
              "success": false,
              "status": 401,
              "code": "UNAUTHORIZED",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "인증이 필요합니다."
            }
            """;

    String ADMIN_ACCESS_FORBIDDEN_EXAMPLE = """
            {
              "success": false,
              "status": 403,
              "code": "ADMIN_ACCESS_FORBIDDEN",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "관리자 권한이 필요합니다."
            }
            """;

    String ADMIN_SELF_ACTION_FORBIDDEN_EXAMPLE = """
            {
              "success": false,
              "status": 403,
              "code": "ADMIN_SELF_ACTION_FORBIDDEN",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "관리자 본인 계정에는 수행할 수 없는 작업입니다."
            }
            """;

    String USER_NOT_FOUND_EXAMPLE = """
            {
              "success": false,
              "status": 404,
              "code": "USER_NOT_FOUND",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "사용자를 찾을 수 없습니다."
            }
            """;

    String USER_ALREADY_DELETED_EXAMPLE = """
            {
              "success": false,
              "status": 409,
              "code": "USER_ALREADY_DELETED",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "이미 탈퇴 처리된 사용자입니다."
            }
            """;

    String USER_NOT_DELETED_EXAMPLE = """
            {
              "success": false,
              "status": 409,
              "code": "USER_NOT_DELETED",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "탈퇴 처리된 사용자가 아닙니다."
            }
            """;

    String INTERNAL_SERVER_ERROR_EXAMPLE = """
            {
              "success": false,
              "status": 500,
              "code": "INTERNAL_SERVER_ERROR",
              "timestamp": "2026-05-17T08:00:00Z",
              "message": "서버 내부 에러"
            }
            """;

    @Operation(
            summary = "사용자 목록 조회",
            description = "관리자가 전체 사용자를 페이지 단위로 조회합니다. 이메일, 이름, 전화번호 키워드 검색과 "
                    + "권한, 탈퇴 여부, 가입 방식, 이메일 인증 여부, 프로필 완성 여부 필터를 지원합니다. "
                    + "목록은 id 내림차순으로 정렬됩니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 목록 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PageResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 목록 조회 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "code": "SUCCESS",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "사용자 목록 조회에 성공했습니다.",
                                              "result": {
                                                "content": [
                                                  {
                                                    "id": 10,
                                                    "email": "user@example.com",
                                                    "name": "홍길동",
                                                    "phoneNumber": "010-1234-5678",
                                                    "birthDate": "2000-01-01",
                                                    "authProvider": "LOCAL",
                                                    "profileCompleted": true,
                                                    "emailVerified": false,
                                                    "deleted": false,
                                                    "role": "USER"
                                                  }
                                                ],
                                                "page": 0,
                                                "size": 20,
                                                "totalElements": 1,
                                                "totalPages": 1,
                                                "first": true,
                                                "last": true
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "page, size 검증 실패 또는 쿼리 파라미터 타입 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "page, size 검증 실패",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "VALIDATION_FAILED",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "요청 값이 올바르지 않습니다.",
                                                      "error": {
                                                        "getUsers.page": "page는 0 이상이어야 합니다.",
                                                        "getUsers.size": "size는 100 이하여야 합니다."
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "쿼리 파라미터 타입 오류",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "INVALID_PARAMETER_TYPE",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "파라미터 'role' 값 'MANAGER' 이(가) 올바르지 않습니다."
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = UNAUTHORIZED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = ADMIN_ACCESS_FORBIDDEN_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INTERNAL_SERVER_ERROR_EXAMPLE)))
    })
    ResponseEntity<ApiResponse<PageResponse<AdminUserResponse>>> getUsers(
            @Parameter(hidden = true) AuthUser authUser,
            @Parameter(description = "페이지 번호. 0부터 시작합니다.", example = "0")
            @Min(value = 0, message = "page는 0 이상이어야 합니다.")
            int page,
            @Parameter(description = "페이지 크기. 1 이상 100 이하입니다.", example = "20")
            @Min(value = 1, message = "size는 1 이상이어야 합니다.")
            @Max(value = 100, message = "size는 100 이하여야 합니다.")
            int size,
            @Parameter(description = "이메일, 이름, 전화번호 검색어", example = "user") String keyword,
            @Parameter(description = "사용자 권한", example = "USER") UserRole role,
            @Parameter(description = "탈퇴 여부", example = "false") Boolean deleted,
            @Parameter(description = "가입 방식", example = "LOCAL") AuthProvider authProvider,
            @Parameter(description = "이메일 인증 여부", example = "true") Boolean emailVerified,
            @Parameter(description = "프로필 완성 여부", example = "true") Boolean profileCompleted
    );

    @Operation(
            summary = "사용자 상세 조회",
            description = "관리자가 특정 사용자의 상세 정보를 조회합니다. 탈퇴 처리된 사용자도 조회할 수 있습니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 상세 조회 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(name = "사용자 상세 조회 성공", value = USER_SUCCESS_EXAMPLE))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "userId 타입 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INVALID_PARAMETER_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = UNAUTHORIZED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = ADMIN_ACCESS_FORBIDDEN_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_NOT_FOUND_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INTERNAL_SERVER_ERROR_EXAMPLE)))
    })
    ResponseEntity<ApiResponse<AdminUserResponse>> getUser(
            @Parameter(hidden = true) AuthUser authUser,
            @Parameter(description = "조회할 사용자 ID", example = "10") Long userId
    );

    @Operation(
            summary = "사용자 정보 수정",
            description = "관리자가 사용자 이메일, 이름, 연락처, 생년월일을 수정합니다. 이메일 변경 시 중복 여부를 검증합니다."
    )
    @RequestBody(
            required = true,
            description = "사용자 정보 수정 요청",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AdminUserUpdateRequest.class),
                    examples = @ExampleObject(
                            name = "사용자 정보 수정 요청",
                            value = """
                                    {
                                      "email": "user@example.com",
                                      "name": "홍길동",
                                      "phoneNumber": "010-1234-5678",
                                      "birthDate": "2000-01-01"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 수정 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 정보 수정 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "code": "SUCCESS",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "사용자 정보 수정에 성공했습니다.",
                                              "result": {
                                                "id": 10,
                                                "email": "user@example.com",
                                                "name": "홍길동",
                                                "phoneNumber": "010-1234-5678",
                                                "birthDate": "2000-01-01",
                                                "authProvider": "LOCAL",
                                                "profileCompleted": true,
                                                "emailVerified": false,
                                                "deleted": false,
                                                "role": "USER"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 값 오류, JSON 형식 오류, userId 타입 오류, DB 무결성 오류",
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
                                                        "name": "이름은 필수입니다.",
                                                        "phoneNumber": "ex) 010-0000-0000 형식입니다.",
                                                        "birthDate": "미래 날짜는 작성할 수 없습니다."
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "JSON 형식 오류",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "INVALID_REQUEST_FORMAT",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "올바르지 않은 요청 형식입니다."
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(name = "userId 타입 오류", value = INVALID_PARAMETER_EXAMPLE),
                                    @ExampleObject(
                                            name = "DB 무결성 오류",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "DATA_INTEGRITY_VIOLATION",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "데이터 무결성 오류가 발생했습니다."
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = UNAUTHORIZED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = ADMIN_ACCESS_FORBIDDEN_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_NOT_FOUND_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 사용 중인 이메일",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "이미 사용 중인 이메일",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 409,
                                              "code": "MEMBER_ALREADY_EXISTS",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "이미 사용중인 이메일입니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INTERNAL_SERVER_ERROR_EXAMPLE)))
    })
    ResponseEntity<ApiResponse<AdminUserResponse>> updateUser(
            @Parameter(hidden = true) AuthUser authUser,
            @Parameter(description = "수정할 사용자 ID", example = "10") Long userId,
            @Valid AdminUserUpdateRequest request
    );

    @Operation(
            summary = "사용자 권한 변경",
            description = "관리자가 사용자의 권한을 USER 또는 ADMIN으로 변경합니다. 관리자 본인 계정의 권한은 변경할 수 없습니다."
    )
    @RequestBody(
            required = true,
            description = "사용자 권한 변경 요청",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AdminUserRoleUpdateRequest.class),
                    examples = @ExampleObject(
                            name = "사용자 권한 변경 요청",
                            value = """
                                    {
                                      "role": "ADMIN"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 권한 변경 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 권한 변경 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "code": "SUCCESS",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "사용자 권한 변경에 성공했습니다.",
                                              "result": {
                                                "id": 10,
                                                "email": "user@example.com",
                                                "name": "홍길동",
                                                "phoneNumber": "010-1234-5678",
                                                "birthDate": "2000-01-01",
                                                "authProvider": "LOCAL",
                                                "profileCompleted": true,
                                                "emailVerified": false,
                                                "deleted": false,
                                                "role": "ADMIN"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "role 필수값 누락, role enum 값 오류, JSON 형식 오류, userId 타입 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "role 필수값 누락",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "VALIDATION_FAILED",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "요청 값이 올바르지 않습니다.",
                                                      "error": {
                                                        "role": "권한은 필수입니다."
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "role enum 값 오류 또는 JSON 형식 오류",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "code": "INVALID_REQUEST_FORMAT",
                                                      "timestamp": "2026-05-17T08:00:00Z",
                                                      "message": "올바르지 않은 요청 형식입니다."
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(name = "userId 타입 오류", value = INVALID_PARAMETER_EXAMPLE)
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = UNAUTHORIZED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "관리자 권한 없음 또는 관리자 본인 계정 작업 시도",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "관리자 권한 없음", value = ADMIN_ACCESS_FORBIDDEN_EXAMPLE),
                                    @ExampleObject(name = "관리자 본인 계정 작업 시도", value = ADMIN_SELF_ACTION_FORBIDDEN_EXAMPLE)
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_NOT_FOUND_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 탈퇴 처리된 사용자", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_ALREADY_DELETED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INTERNAL_SERVER_ERROR_EXAMPLE)))
    })
    ResponseEntity<ApiResponse<AdminUserResponse>> updateUserRole(
            @Parameter(hidden = true) AuthUser authUser,
            @Parameter(description = "권한을 변경할 사용자 ID", example = "10") Long userId,
            @Valid AdminUserRoleUpdateRequest request
    );

    @Operation(
            summary = "사용자 탈퇴 처리",
            description = "관리자가 사용자를 탈퇴 처리합니다. deleted 값을 true로 변경하고 해당 사용자의 refresh token을 삭제합니다. "
                    + "관리자 본인 계정은 탈퇴 처리할 수 없습니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 탈퇴 처리 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 탈퇴 처리 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "code": "SUCCESS",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "사용자 탈퇴 처리에 성공했습니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "userId 타입 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INVALID_PARAMETER_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = UNAUTHORIZED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "관리자 권한 없음 또는 관리자 본인 계정 작업 시도",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "관리자 권한 없음", value = ADMIN_ACCESS_FORBIDDEN_EXAMPLE),
                                    @ExampleObject(name = "관리자 본인 계정 작업 시도", value = ADMIN_SELF_ACTION_FORBIDDEN_EXAMPLE)
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_NOT_FOUND_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 탈퇴 처리된 사용자", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_ALREADY_DELETED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INTERNAL_SERVER_ERROR_EXAMPLE)))
    })
    ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(hidden = true) AuthUser authUser,
            @Parameter(description = "탈퇴 처리할 사용자 ID", example = "10") Long userId
    );

    @Operation(
            summary = "사용자 복구",
            description = "관리자가 탈퇴 처리된 사용자를 복구합니다. deleted 값을 false로 변경합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 복구 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 복구 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "code": "SUCCESS",
                                              "timestamp": "2026-05-17T08:00:00Z",
                                              "message": "사용자 복구에 성공했습니다.",
                                              "result": {
                                                "id": 10,
                                                "email": "user@example.com",
                                                "name": "홍길동",
                                                "phoneNumber": "010-1234-5678",
                                                "birthDate": "2000-01-01",
                                                "authProvider": "LOCAL",
                                                "profileCompleted": true,
                                                "emailVerified": false,
                                                "deleted": false,
                                                "role": "USER"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "userId 타입 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INVALID_PARAMETER_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = UNAUTHORIZED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 필요", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = ADMIN_ACCESS_FORBIDDEN_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_NOT_FOUND_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "탈퇴 처리된 사용자가 아님", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = USER_NOT_DELETED_EXAMPLE))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = INTERNAL_SERVER_ERROR_EXAMPLE)))
    })
    ResponseEntity<ApiResponse<AdminUserResponse>> restoreUser(
            @Parameter(hidden = true) AuthUser authUser,
            @Parameter(description = "복구할 사용자 ID", example = "10") Long userId
    );
}
