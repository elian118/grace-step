package com.postelian.backend.domain.classroom.controller;

import com.postelian.backend.domain.classroom.dto.VocabularyDto.BatchUploadResult;
import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyRequest;
import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyResponse;
import com.postelian.backend.domain.classroom.service.VocabularyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Vocabulary API", description = "영단어 관리 관련 API")
@RestController
@RequestMapping("/api/v1/vocabularies")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @Operation(summary = "엑셀 대량 단어 업로드", description = "완성된 영단어 엑셀 파일을 업로드하여 1,000건 단위 배치로 DB에 저장합니다.")
    @PostMapping(value = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BatchUploadResult> uploadExcel(
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "작업 수행 사용자 ID", example = "admin")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        BatchUploadResult result = vocabularyService.uploadVocabulariesFromExcel(file, userId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "영단어 다건 등록", description = "영단어 목록을 한 번에 등록합니다.")
    @PostMapping("/batch")
    public ResponseEntity<List<VocabularyResponse>> registerVocabularies(
            @RequestBody @Valid List<VocabularyRequest> requests,
            @Parameter(description = "작업 수행 사용자 ID", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        List<VocabularyResponse> responses = vocabularyService.registerVocabularies(requests, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @Operation(summary = "영단어 전체 조회", description = "모든 영단어 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<VocabularyResponse>> getAllVocabularies() {
        return ResponseEntity.ok(vocabularyService.getAllVocabularies());
    }

    @Operation(summary = "영단어 단건 조회", description = "영단어 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<VocabularyResponse> getVocabulary(@PathVariable Long id) {
        return ResponseEntity.ok(vocabularyService.getVocabulary(id));
    }

    @Operation(summary = "영단어 다건 삭제", description = "영단어 목록을 삭제(Soft Delete)합니다.")
    @DeleteMapping
    public ResponseEntity<Void> deleteVocabularies(
            @RequestBody List<Long> ids,
            @Parameter(description = "작업 수행 사용자 ID", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        vocabularyService.deleteVocabularies(ids, userId);
        return ResponseEntity.noContent().build();
    }
}
