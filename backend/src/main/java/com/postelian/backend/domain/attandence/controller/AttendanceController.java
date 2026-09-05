package com.postelian.backend.domain.attandence.controller;

import com.postelian.backend.domain.common.file.dto.FileResponseDto;
import com.postelian.backend.domain.common.file.dto.FileUploadRequestDto;
import com.postelian.backend.domain.common.file.service.FileService;
import com.postelian.backend.domain.attandence.dto.AttendanceDto.AttendanceRequest;
import com.postelian.backend.domain.attandence.dto.AttendanceDto.AttendanceResponse;
import com.postelian.backend.domain.attandence.dto.AttendanceSearchRequestDto;
import com.postelian.backend.domain.attandence.service.AttendanceService;
import com.postelian.backend.global.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Student Attendance API", description = "학생 출석 관리 관련 API")
@RestController
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final FileService fileService;

    @Operation(summary = "출석 기록 및 수정 (단건)", description = "특정 학생의 당일 출석 여부와 메모를 기록하거나 기존 기록을 수정합니다.")
    @PostMapping
    public ResponseEntity<AttendanceResponse> saveOrUpdateAttendance(
            @RequestBody @Valid AttendanceRequest request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        AttendanceResponse response = attendanceService.saveOrUpdateAttendance(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "출석 기록 및 수정 (다건)", description = "학생 전원의 출석 정보를 한 번에 기록하거나 수정합니다.")
    @PostMapping("/bulk")
    public ResponseEntity<List<AttendanceResponse>> saveOrUpdateAttendances(
            @RequestBody @Valid List<AttendanceRequest> requests,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        List<AttendanceResponse> response = attendanceService.saveOrUpdateAttendances(requests, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "출석 기록 목록 조회", description = "조회 기간, 학생명, 출석 상태별로 필터링된 출석 기록 목록을 페이지 단위로 조회합니다.")
    @PostMapping("/list")
    public ResponseEntity<PageResponse<AttendanceResponse>> getAttendanceList(
            @RequestBody AttendanceSearchRequestDto dto) {
        PageResponse<AttendanceResponse> response = attendanceService.getAttendanceList(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "출석 기록 삭제", description = "특정 출석 기록(ID)을 삭제합니다.")
    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(
            @PathVariable Long attendanceId,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        attendanceService.deleteAttendance(attendanceId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "출석부 파일 업로드", description = "출석부 PDF 파일을 업로드합니다.")
    @PostMapping(value = "/files", consumes = {"multipart/form-data"})
    public ResponseEntity<FileResponseDto> uploadAttendanceFile(
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {

        FileUploadRequestDto.Single request = new FileUploadRequestDto.Single();
        request.setFile(file);
        request.setFileFolderKey(UUID.randomUUID().toString());
        request.setType("ATTENDANCE");

        FileResponseDto savedFile = fileService.uploadFile(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    @Operation(summary = "출석부 파일 목록 조회 (날짜 범위)", description = "조회 기간 내 업로드된 출석부 파일 목록을 조회합니다.")
    @GetMapping("/files")
    public ResponseEntity<List<FileResponseDto>> getAttendanceFiles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<FileResponseDto> files = fileService.getFileEntitiesByTypeAndDate("ATTENDANCE", startDate, endDate);
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "출석부 파일 삭제", description = "특정 출석부 파일을 삭제합니다.")
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Void> deleteAttendanceFile(
            @PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
