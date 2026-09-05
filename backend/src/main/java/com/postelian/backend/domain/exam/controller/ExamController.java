package com.postelian.backend.domain.exam.controller;

import com.postelian.backend.domain.exam.dto.ExamRequestDto;
import com.postelian.backend.domain.exam.entity.ExamQuestion;
import com.postelian.backend.domain.exam.repository.ExamQuestionRepository;
import com.postelian.backend.domain.exam.service.ExamGenerationService;
import com.postelian.backend.domain.exam.service.ExamPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Exam API", description = "시험지 관리 관련 API")
@RestController
@RequestMapping("/api/v1/classes/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamGenerationService examGenerationService;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamPdfService examPdfService;

    @Operation(summary = "시험지 자동 생성 및 저장", description = "조건에 맞는 시험지를 AI로 생성하여 저장합니다.")
    @PostMapping
    public ResponseEntity<List<ExamQuestion>> generateAndSaveExam(
            @RequestBody ExamRequestDto request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) throws IOException {
        List<ExamQuestion> savedExam = examGenerationService.generateAndSaveExam(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
    }

    @Operation(summary = "시험지 조회", description = "제목으로 시험지 문항 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ExamQuestion>> getExamByTitle(@RequestParam String title) {
        List<ExamQuestion> exam = examQuestionRepository.findByTitle(title);
        return ResponseEntity.ok(exam);
    }

    @Operation(summary = "시험지 PDF 업로드 및 매핑", description = "생성된 시험지 PDF를 저장하고 시험지 데이터와 매핑합니다.")
    @PostMapping(value = "/pdf", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> uploadExamPdf(
            @RequestParam String title,
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) throws IOException {
        examPdfService.uploadExamPdf(title, file, userId);
        return ResponseEntity.noContent().build();
    }
}
