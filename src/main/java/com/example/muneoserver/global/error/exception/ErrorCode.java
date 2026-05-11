package com.example.muneoserver.global.error.exception;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 내부 에러"),
    VALIDATION_FAILED(400, "VALIDATION_FAILED", "요청 값이 올바르지 않습니다."),
    INVALID_PARAMETER_TYPE(400, "INVALID_PARAMETER_TYPE", "적절하지 않은 파라미터 타입입니다."),
    INVALID_REQUEST_FORMAT(400, "INVALID_REQUEST_FORMAT", "올바르지 않은 요청 형식입니다."),
    DATA_INTEGRITY_VIOLATION(400, "DATA_INTEGRITY_VIOLATION", "데이터 무결성 오류가 발생했습니다."),
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증이 필요합니다."),
    MEMBER_ALREADY_EXISTS(409, "MEMBER_ALREADY_EXISTS", "이미 가입된 이메일입니다."),
    INVALID_LOGIN_INFO(401, "INVALID_LOGIN_INFO", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(401, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(401, "REFRESH_TOKEN_NOT_FOUND", "만료되었거나 로그아웃된 토큰입니다."),
    REFRESH_TOKEN_MISMATCH(401, "REFRESH_TOKEN_MISMATCH", "리프레시 토큰이 일치하지 않습니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int status() {
        return status;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
