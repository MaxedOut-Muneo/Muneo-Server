package com.example.muneoserver.domain.file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.muneoserver.domain.file.dto.PresignedUrlRequest;
import com.example.muneoserver.domain.file.dto.PresignedUrlResponse;
import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.UserRole;
import com.example.muneoserver.global.config.s3.S3Properties;
import com.example.muneoserver.global.error.exception.CommonException;
import com.example.muneoserver.global.error.exception.ErrorCode;
import com.example.muneoserver.global.security.auth.AuthUser;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

class FileServiceImplTest {

    private S3Presigner s3Presigner;
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        s3Presigner = S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test-access-key", "test-secret-key")))
                .build();

        fileService = new FileServiceImpl(
                s3Presigner,
                new S3Properties(
                        "test-bucket",
                        "ap-northeast-2",
                        Duration.ofMinutes(10),
                        "estimate",
                        "post"
                )
        );
    }

    @AfterEach
    void tearDown() {
        s3Presigner.close();
    }

    @Test
    void issuePresignedUrlPreservesFilenameExtensionAndSignsContentType() {
        AuthUser authUser = new AuthUser(1L, "test@example.com", AuthProvider.LOCAL, true, UserRole.USER);
        PresignedUrlRequest request = new PresignedUrlRequest("Post", "room.jpg", "image/jpeg");

        PresignedUrlResponse response = fileService.issuePresignedUrl(authUser, request);

        assertThat(response.type()).isEqualTo("Post");
        assertThat(response.key()).matches("post/1/[0-9a-f\\-]+\\.jpg");
        assertThat(response.fileUrl()).endsWith(response.key());
        assertThat(response.uploadMethod()).isEqualTo("PUT");
        assertThat(response.contentType()).isEqualTo("image/jpeg");
        assertThat(decodeQueryParam(response.uploadUrl(), "X-Amz-SignedHeaders")).contains("content-type");
    }

    @Test
    void issuePresignedUrlThrowsWhenFilenameHasNoExtension() {
        AuthUser authUser = new AuthUser(1L, "test@example.com", AuthProvider.LOCAL, true, UserRole.USER);
        PresignedUrlRequest request = new PresignedUrlRequest("Estimate", "estimate", "application/pdf");

        assertThatThrownBy(() -> fileService.issuePresignedUrl(authUser, request))
                .isInstanceOf(CommonException.class)
                .satisfies(exception -> {
                    CommonException commonException = (CommonException) exception;
                    assertThat(commonException.getErrorCode()).isEqualTo(ErrorCode.VALIDATION_FAILED);
                    assertThat(commonException.getErrors()).containsEntry("filename", "파일명에 확장자가 필요합니다.");
                });
    }

    @Test
    void issuePresignedUrlTrimsS3ConfigurationValues() {
        FileServiceImpl serviceWithSpacedProperties = new FileServiceImpl(
                s3Presigner,
                new S3Properties(
                        " gachon-21-s3 ",
                        " ap-northeast-2 ",
                        Duration.ofMinutes(10),
                        " estimate ",
                        " post "
                )
        );
        AuthUser authUser = new AuthUser(1L, "test@example.com", AuthProvider.LOCAL, true, UserRole.USER);
        PresignedUrlRequest request = new PresignedUrlRequest("Post", "room.jpg", "image/jpeg");

        PresignedUrlResponse response = serviceWithSpacedProperties.issuePresignedUrl(authUser, request);

        assertThat(response.key()).startsWith("post/1/");
        assertThat(response.uploadUrl()).contains("gachon-21-s3");
        assertThat(response.uploadUrl()).contains("post/1/");
        assertThat(response.uploadUrl()).doesNotContain("%20");
        assertThat(response.uploadUrl()).doesNotContain(" ");
        assertThat(response.fileUrl()).startsWith("https://gachon-21-s3.s3.ap-northeast-2.amazonaws.com/post/1/");
    }

    private String decodeQueryParam(String url, String name) {
        String query = URI.create(url).getRawQuery();

        for (String entry : query.split("&")) {
            int separatorIndex = entry.indexOf('=');
            String key = separatorIndex >= 0 ? entry.substring(0, separatorIndex) : entry;
            String value = separatorIndex >= 0 ? entry.substring(separatorIndex + 1) : "";

            if (name.equals(key)) {
                return URLDecoder.decode(value, StandardCharsets.UTF_8);
            }
        }

        throw new IllegalArgumentException("Query parameter not found: " + name);
    }
}
