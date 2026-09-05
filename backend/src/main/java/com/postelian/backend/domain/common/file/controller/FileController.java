package com.postelian.backend.domain.common.file.controller;

import com.postelian.backend.domain.common.file.dto.FileResponseDto;
import com.postelian.backend.domain.common.file.dto.FileUploadRequestDto;
import com.postelian.backend.domain.common.file.entity.File;
import com.postelian.backend.domain.common.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "File API", description = "파일 제어 API")
@RestController
@RequestMapping("/api/v1/common/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "파일 단건 업로드")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> uploadFile(
            @ModelAttribute FileUploadRequestDto.Single dto,
            @RequestHeader(value = "X-User-Id", required = false) String createdBy
    ) {
        FileResponseDto response = fileService.uploadFile(dto, createdBy);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "파일 다건 업로드")
    @PostMapping(value = "/upload/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FileResponseDto>> uploadFiles(
            @ModelAttribute FileUploadRequestDto.Multiple dto,
            @RequestHeader(value = "X-User-Id", required = false) String createdBy
    ) {
        List<FileResponseDto> response = fileService.uploadFiles(dto, createdBy);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "파일 다운로드")
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        File file = fileService.getFileEntity(fileId);
        Resource resource = fileService.downloadFile(fileId);

        String encodedOriginalFileName = UriUtils.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Operation(summary = "파일 삭제")
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
