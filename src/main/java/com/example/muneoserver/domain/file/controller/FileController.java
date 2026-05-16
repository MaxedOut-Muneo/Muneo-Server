package com.example.muneoserver.domain.file.controller;

import com.example.muneoserver.domain.file.controller.docs.FileControllerDocs;
import com.example.muneoserver.domain.file.dto.PresignedUrlRequest;
import com.example.muneoserver.domain.file.dto.PresignedUrlResponse;
import com.example.muneoserver.domain.file.service.FileService;
import com.example.muneoserver.global.dto.ApiResponse;
import com.example.muneoserver.global.security.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController implements FileControllerDocs {

    private final FileService fileService;

    @Override
    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> issuePresignedUrl(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam
            String type,
            @RequestParam
            String filename,
            @RequestParam
            String contentType
    ) {
        PresignedUrlRequest request = new PresignedUrlRequest(type, filename, contentType);
        return ResponseEntity.ok(ApiResponse.success(fileService.issuePresignedUrl(authUser, request), "Presigned URL 발급에 성공했습니다."));
    }
}
