package com.postelian.backend.domain.attandence.entity;

import com.postelian.backend.domain.student.entity.StudentProfile;
import com.postelian.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(
        name = "attendances",
        uniqueConstraints = {
                // 한 학생이 같은 날짜에 중복 출석 기록이 생기지 않도록 방지
                @UniqueConstraint(
                        name = "uk_student_attendance_date",
                        columnNames = {"student_profile_id", "attendance_date"}
                )
        },
        indexes = {
                @Index(name = "idx_attendance_date", columnList = "attendance_date"),
                @Index(name = "idx_student_attendance", columnList = "student_profile_id, attendance_date")
        }
)
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1 관계 - 출석 대상 학생 프로필 (FK: student_profile_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    // 출석 일자 (시간 제외 YYYY-MM-DD만 저장)
    @Column(nullable = false)
    private LocalDate attendanceDate;

    // 출석 상태 (출석 / 지각 / 결석 / 조퇴)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    // 비고 / 사유 (예: 병결, 개인 사정 지각 등)
    @Column(length = 255)
    private String note;

    @Builder
    public Attendance(StudentProfile studentProfile, LocalDate attendanceDate, AttendanceStatus status, String note, String createdBy) {
        this.studentProfile = studentProfile;
        this.attendanceDate = attendanceDate;
        this.status = status != null ? status : AttendanceStatus.ATTENDANCE;
        this.note = note;
        if (createdBy != null) {
            recordCreation(createdBy);
        }
    }

    // --- 비즈니스 메서드 ---

    // 출석 정보 수정
    public void updateAttendance(AttendanceStatus status, String note, String updatedBy) {
        this.status = status;
        this.note = note;
        recordModification(updatedBy);
    }
}
