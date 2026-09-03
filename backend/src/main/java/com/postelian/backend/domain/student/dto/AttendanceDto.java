package com.postelian.backend.domain.student.dto;

import com.postelian.backend.domain.student.entity.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendanceDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "출석 기록 및 수정 요청 DTO")
    public static class AttendanceRequest {

        @NotNull
        @Schema(description = "학생 프로필 ID", example = "1")
        private Long studentProfileId;

        @NotNull
        @Schema(description = "출석 일자", example = "2026-08-30")
        private LocalDate attendanceDate;

        @NotNull
        @Schema(description = "출석 상태", example = "ATTENDANCE")
        private AttendanceStatus status;

        @Schema(description = "특이사항 및 메모", example = "개인 사정으로 인한 지각")
        private String note;

        @Builder
        public AttendanceRequest(Long studentProfileId, LocalDate attendanceDate, AttendanceStatus status, String note) {
            this.studentProfileId = studentProfileId;
            this.attendanceDate = attendanceDate;
            this.status = status;
            this.note = note;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "출석 기록 응답 DTO")
    public static class AttendanceResponse {

        @Schema(description = "출석 기록 ID", example = "1")
        private Long id;

        @Schema(description = "학생 ID", example = "1")
        private Long studentProfileId;

        @Schema(description = "학생 이름", example = "홍길동")
        private String studentName;

        @Schema(description = "출석 일자", example = "2026-08-30")
        private LocalDate attendanceDate;

        @Schema(description = "출석 상태", example = "ATTENDANCE")
        private AttendanceStatus status;

        @Schema(description = "특이사항 및 메모", example = "정상 출석")
        private String note;

        @Builder
        public AttendanceResponse(Long id, Long studentProfileId, String studentName, LocalDate attendanceDate, AttendanceStatus status, String note) {
            this.id = id;
            this.studentProfileId = studentProfileId;
            this.studentName = studentName;
            this.attendanceDate = attendanceDate;
            this.status = status;
            this.note = note;
        }
    }
}
