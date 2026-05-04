package com.example.muneoserver.global.error.exception;

import java.util.Map;

public class CommonException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String customMessage;
    private final Map<String, String> errors;

    public CommonException(ErrorCode errorCode) {
        this(errorCode, null, null);
    }

    public CommonException(ErrorCode errorCode, String customMessage) {
        this(errorCode, customMessage, null);
    }

    public CommonException(ErrorCode errorCode, Map<String, String> errors) {
        this(errorCode, null, errors);
    }

    private CommonException(ErrorCode errorCode, String customMessage, Map<String, String> errors) {
        super(customMessage != null ? customMessage : errorCode.message());
        this.errorCode = errorCode;
        this.customMessage = customMessage;
        this.errors = errors;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
