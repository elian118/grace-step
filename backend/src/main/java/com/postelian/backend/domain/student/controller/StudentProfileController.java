package com.postelian.backend.domain.student.controller;

import com.postelian.backend.domain.student.dto.StudentProfileDto.StudentProfileRequest;
import com.postelian.backend.domain.student.dto.StudentProfileDto.StudentProfileResponse;
import com.postelian.backend.domain.student.entity.GradeLevel;
import com.postelian.backend.domain.student.service.StudentProfileService;
import com.postelian.backend.global.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Student Profile API", description = "학생 프로필 관리 관련 API")
@RestController
@RequestMapping("/api/v1/students/profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @Operation(summary = "학생 프로필 다건 조회", description = "필터 조건에 따라 학생 프로필 목록을 페이지 단위로 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponse<StudentProfileResponse>> getStudentProfileList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) GradeLevel gradeLevel,
            @RequestParam(required = false) String schoolName,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault Pageable pageable) {
        PageResponse<StudentProfileResponse> response = studentProfileService.getStudentProfileList(name, gradeLevel, schoolName, isActive, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "학생 프로필 등록", description = "새로운 학생 프로필을 등록합니다.")
    @PostMapping
    public ResponseEntity<StudentProfileResponse> registerStudent(
            @RequestBody @Valid StudentProfileRequest request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        StudentProfileResponse response = studentProfileService.registerStudent(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "학생 프로필 수정", description = "기존 학생 프로필 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<StudentProfileResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody @Valid StudentProfileRequest request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        StudentProfileResponse response = studentProfileService.updateStudent(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "학생 프로필 삭제", description = "학생 프로필을 삭제(Soft Delete)합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        studentProfileService.deleteStudent(id, userId);
        return ResponseEntity.noContent().build();
    }
}
