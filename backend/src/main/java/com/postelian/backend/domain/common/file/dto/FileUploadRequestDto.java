package com.postelian.backend.domain.common.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUploadRequestDto {

    @Getter
    @Setter
    @Schema(description = "파일 단건 업로드 요청 DTO")
    public static class Single {
        private MultipartFile file;
        private String fileFolderKey;
        private String type;
    }

    @Getter
    @Setter
    @Schema(description = "파일 다건 업로드 요청 DTO")
    public static class Multiple {
        private List<MultipartFile> files;
        private String fileFolderKey;
        private String type;
    }
}
