package com.postelian.backend.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "파일 메타데이터 응답 DTO")
public class FileDto {

    private Long id;
    private String originalFilename;
    private String fileFolderKey;
    private String type;
}
