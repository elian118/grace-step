package com.postelian.backend.domain.classroom.controller;

import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyRequest;
import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyResponse;
import com.postelian.backend.domain.classroom.service.VocabularyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vocabulary API", description = "영단어 관리 관련 API")
@RestController
@RequestMapping("/api/v1/vocabularies")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @Operation(summary = "영단어 다건 등록", description = "영단어 목록을 한 번에 등록합니다.")
    @PostMapping("/batch")
    public ResponseEntity<List<VocabularyResponse>> registerVocabularies(
            @RequestBody @Valid List<VocabularyRequest> requests,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
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

    @Operation(summary = "영단어 등록/수정(Upsert)", description = "단어 기준으로 존재하면 수정, 없으면 등록합니다.")
    @PutMapping
    public ResponseEntity<VocabularyResponse> upsertVocabulary(
            @RequestBody @Valid VocabularyRequest request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        return ResponseEntity.ok(vocabularyService.upsertVocabulary(request, userId));
    }

    @Operation(summary = "영단어 다건 삭제", description = "영단어 목록을 삭제(Soft Delete)합니다.")
    @DeleteMapping
    public ResponseEntity<Void> deleteVocabularies(
            @RequestBody List<Long> ids,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        vocabularyService.deleteVocabularies(ids, userId);
        return ResponseEntity.noContent().build();
    }
}
