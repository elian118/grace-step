package com.postelian.backend.domain.student.controller;

import com.postelian.backend.domain.student.dto.AttendanceDto.AttendanceRequest;
import com.postelian.backend.domain.student.dto.AttendanceDto.AttendanceResponse;
import com.postelian.backend.domain.student.entity.AttendanceStatus;
import com.postelian.backend.domain.student.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import com.postelian.backend.global.common.PageResponse;
import org.springframework.data.domain.Pageable;

@Tag(name = "Student Attendance API", description = "학생 출석 관리 관련 API")
@RestController
@RequestMapping("/api/v1/students/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "출석 기록 및 수정", description = "특정 학생의 당일 출석 여부와 메모를 기록하거나 기존 기록을 수정합니다.")
    @PostMapping
    public ResponseEntity<AttendanceResponse> saveOrUpdateAttendance(
            @RequestBody @Valid AttendanceRequest request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        AttendanceResponse response = attendanceService.saveOrUpdateAttendance(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "출석 기록 목록 조회", description = "조회 기간, 학생명, 출석 상태별로 필터링된 출석 기록 목록을 페이지 단위로 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponse<AttendanceResponse>> getAttendanceList(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) AttendanceStatus status,
            Pageable pageable) {
        PageResponse<AttendanceResponse> response = attendanceService.getAttendanceList(startDate, endDate, studentName, status, pageable);
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
}
