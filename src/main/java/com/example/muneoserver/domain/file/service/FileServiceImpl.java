package com.example.muneoserver.domain.file.service;

import com.example.muneoserver.domain.file.domain.FileUploadType;
import com.example.muneoserver.domain.file.dto.PresignedUrlRequest;
import com.example.muneoserver.domain.file.dto.PresignedUrlResponse;
import com.example.muneoserver.global.config.s3.S3Properties;
import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import com.example.muneoserver.global.security.auth.AuthUser;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Presigner s3Presigner;
    private final S3Properties s3Properties;

    @Override
    public PresignedUrlResponse issuePresignedUrl(AuthUser authUser, PresignedUrlRequest request) {
        validateAuthenticated(authUser);
        validateS3Configuration();
        validateRequest(request);

        FileUploadType uploadType = FileUploadType.from(request.type());
        String extension = extractExtension(request.filename());
        String key = buildKey(uploadType, authUser.id(), extension);
        Instant expiresAt = Instant.now().plus(s3Properties.presignedUrlExpiration());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.bucket())
                    .key(key)
                    .contentType(request.contentType())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(s3Properties.presignedUrlExpiration())
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            return new PresignedUrlResponse(
                    uploadType.requestType(),
                    key,
                    presignedRequest.url().toString(),
                    buildFileUrl(key),
                    "PUT",
                    request.contentType(),
                    expiresAt
            );
        } catch (SdkException e) {
            throw new CommonException(ErrorCode.FILE_UPLOAD_PRESIGNED_URL_GENERATION_FAILED);
        }
    }

    private String buildKey(FileUploadType uploadType, Long userId, String extension) {
        String directory = switch (uploadType) {
            case ESTIMATE -> s3Properties.estimateDirectory();
            case POST -> s3Properties.postDirectory();
        };

        return directory + "/" + userId + "/" + UUID.randomUUID() + "." + extension;
    }

    private String buildFileUrl(String key) {
        return "https://" + s3Properties.bucket() + ".s3." + s3Properties.region() + ".amazonaws.com/" + key;
    }

    private void validateAuthenticated(AuthUser authUser) {
        if (authUser == null) {
            throw new CommonException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void validateS3Configuration() {
        if (isBlank(s3Properties.bucket())
                || isBlank(s3Properties.region())
                || isBlank(s3Properties.estimateDirectory())
                || isBlank(s3Properties.postDirectory())
                || s3Properties.presignedUrlExpiration() == null
                || s3Properties.presignedUrlExpiration().isZero()
                || s3Properties.presignedUrlExpiration().isNegative()) {
            throw new CommonException(ErrorCode.FILE_UPLOAD_CONFIGURATION_INVALID);
        }
    }

    private void validateRequest(PresignedUrlRequest request) {
        if (request == null) {
            throw new CommonException(ErrorCode.INVALID_REQUEST_FORMAT);
        }
        if (isBlank(request.type())) {
            throw new CommonException(ErrorCode.VALIDATION_FAILED, Map.of("type", "업로드 타입은 필수입니다."));
        }
        if (isBlank(request.filename())) {
            throw new CommonException(ErrorCode.VALIDATION_FAILED, Map.of("filename", "파일명은 필수입니다."));
        }
        if (isBlank(request.contentType())) {
            throw new CommonException(ErrorCode.VALIDATION_FAILED, Map.of("contentType", "Content-Type은 필수입니다."));
        }
    }

    private String extractExtension(String filename) {
        String normalizedFilename = extractFilename(filename);
        String extension = StringUtils.getFilenameExtension(normalizedFilename);

        if (!StringUtils.hasText(extension)) {
            throw new CommonException(ErrorCode.VALIDATION_FAILED, Map.of("filename", "파일명에 확장자가 필요합니다."));
        }

        return extension;
    }

    private String extractFilename(String filename) {
        String normalized = filename.trim().replace("\\", "/");
        int lastSlashIndex = normalized.lastIndexOf('/');

        if (lastSlashIndex >= 0) {
            normalized = normalized.substring(lastSlashIndex + 1);
        }

        if (!StringUtils.hasText(normalized)) {
            throw new CommonException(ErrorCode.VALIDATION_FAILED, Map.of("filename", "파일명은 필수입니다."));
        }

        return normalized;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
