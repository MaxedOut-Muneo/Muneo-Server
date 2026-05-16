package com.example.muneoserver.domain.file.service;

import com.example.muneoserver.domain.file.dto.PresignedUrlRequest;
import com.example.muneoserver.domain.file.dto.PresignedUrlResponse;
import com.example.muneoserver.global.security.auth.AuthUser;

public interface FileService {

    PresignedUrlResponse issuePresignedUrl(AuthUser authUser, PresignedUrlRequest request);
}
