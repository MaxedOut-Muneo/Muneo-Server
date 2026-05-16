package com.example.muneoserver.domain.file.domain;

import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import java.util.Arrays;

public enum FileUploadType {
    ESTIMATE("Estimate"),
    POST("Post");

    private final String requestType;

    FileUploadType(String requestType) {
        this.requestType = requestType;
    }

    public String requestType() {
        return requestType;
    }

    public static FileUploadType from(String type) {
        return Arrays.stream(values())
                .filter(value -> value.requestType.equalsIgnoreCase(type) || value.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_FILE_UPLOAD_TYPE));
    }
}
