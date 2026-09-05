package com.postelian.backend.domain.common.file.dto;

import com.postelian.backend.domain.common.file.entity.File;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "파일 메타데이터 응답 DTO")
public class FileResponseDto {

    private Long id;
    private String fileFolderKey;
    private String type;
    private String originalFilename;
    private String storedFilename;
    private Long fileSize;

    public static FileResponseDto from(File file) {
        return FileResponseDto.builder()
                .id(file.getId())
                .fileFolderKey(file.getFileFolderKey())
                .type(file.getType())
                .originalFilename(file.getOriginalFilename())
                .storedFilename(file.getStoredFilename())
                .fileSize(file.getFileSize())
                .build();
    }
}
