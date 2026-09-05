package com.postelian.backend.domain.student.entity;

import com.postelian.backend.domain.attandence.entity.Attendance;
import com.postelian.backend.domain.attandence.entity.AttendanceStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AttendanceTest {

    @Test
    @DisplayName("출석 엔티티 생성 시 등록자 정보가 기록되어야 한다")
    void creationTest() {
        // given
        String userId = "teacher_01";
        Attendance attendance = Attendance.builder()
                .attendanceDate(LocalDate.now())
                .status(AttendanceStatus.ATTENDANCE)
                .createdBy(userId)
                .build();

        // then
        assertThat(attendance.getCreatedBy()).isEqualTo(userId);
        assertThat(attendance.getUpdatedBy()).isEqualTo(userId);
        assertThat(attendance.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("소프트 딜리트 호출 시 삭제 정보가 기록되어야 한다")
    void softDeleteTest() {
        // given
        Attendance attendance = Attendance.builder()
                .attendanceDate(LocalDate.now())
                .status(AttendanceStatus.ATTENDANCE)
                .createdBy("teacher_01")
                .build();

        // when
        String deleteUserId = "admin_01";
        attendance.delete(deleteUserId);

        // then
        assertThat(attendance.getIsDeleted()).isTrue();
        assertThat(attendance.getDeletedBy()).isEqualTo(deleteUserId);
        assertThat(attendance.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("출석 정보 수정 시 수정자 정보가 기록되어야 한다")
    void updateTest() {
        // given
        Attendance attendance = Attendance.builder()
                .attendanceDate(LocalDate.now())
                .status(AttendanceStatus.ATTENDANCE)
                .createdBy("teacher_01")
                .build();

        // when
        String updateUserId = "teacher_02";
        attendance.updateAttendance(AttendanceStatus.LATE, "지각 사유", updateUserId);

        // then
        assertThat(attendance.getStatus()).isEqualTo(AttendanceStatus.LATE);
        assertThat(attendance.getNote()).isEqualTo("지각 사유");
        assertThat(attendance.getUpdatedBy()).isEqualTo(updateUserId);
        assertThat(attendance.getCreatedBy()).isEqualTo("teacher_01"); // 등록자는 유지
    }
}
