package com.example.muneoserver.domain.file.controller.docs;

import com.example.muneoserver.domain.file.dto.PresignedUrlResponse;
import com.example.muneoserver.global.dto.ApiResponse;
import com.example.muneoserver.global.security.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Files", description = "파일 업로드 관련 API")
public interface FileControllerDocs {

    @Operation(
            summary = "S3 Presigned URL 발급",
            description = "업로드 타입, 원본 파일명, Content-Type을 받아 S3 PUT 업로드용 Presigned URL을 발급합니다. "
                    + "업로드 요청 시에는 응답의 contentType 값을 그대로 Content-Type 헤더에 담아 PUT 요청을 보내야 합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 값", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "S3 설정 오류 또는 Presigned URL 생성 실패", content = @Content)
    })
    ResponseEntity<ApiResponse<PresignedUrlResponse>> issuePresignedUrl(
            @Parameter(hidden = true) AuthUser authUser,
            @Parameter(
                    description = "업로드 타입. Estimate는 견적서, Post는 이미지를 의미합니다.",
                    example = "Post"
            ) String type,
            @Parameter(
                    description = "원본 파일명. 마지막 확장자를 기준으로 S3 object key에 동일한 확장자가 보존됩니다.",
                    example = "room.jpg"
            ) String filename,
            @Parameter(
                    description = "업로드 파일의 MIME 타입. 이후 S3 PUT 요청의 Content-Type 헤더에도 같은 값을 사용해야 합니다.",
                    example = "image/jpeg"
            ) String contentType
    );
}
