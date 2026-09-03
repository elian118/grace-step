package com.postelian.backend.domain.student.entity;

import com.postelian.backend.domain.user.entity.User;
import com.postelian.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "student_profiles", indexes = {
        @Index(name = "idx_student_teacher_id", columnList = "teacher_id"),
        @Index(name = "idx_student_is_active", columnList = "is_active")
})
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1:1 관계 - 학생의 기본 계정 정보 (FK: user_id)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // N:1 관계 - 담당 강사 계정 정보 (FK: teacher_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(length = 50)
    private String schoolName; // 학교명 (예: 광명중학교, 하안초등학교)

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GradeLevel gradeLevel; // 학년

    @Column(length = 20)
    private String parentPhoneNumber; // 학부모 연락처

    @Column(nullable = false)
    private Boolean isActive; // 수업 수강/활성 여부 (true: 수강 중, false: 미수강/휴원)

    @Column(columnDefinition = "TEXT")
    private String memo; // 특이사항 및 메모

    @Builder
    public StudentProfile(User user, User teacher, String schoolName, GradeLevel gradeLevel,
                          String phoneNumber, String parentPhoneNumber, Boolean isActive, String memo, String createdBy) {
        this.user = user;
        this.teacher = teacher;
        this.schoolName = schoolName;
        this.gradeLevel = gradeLevel;
        this.parentPhoneNumber = parentPhoneNumber;
        this.isActive = isActive != null ? isActive : true; // 기본값: 활성
        this.memo = memo;
        if (createdBy != null) {
            recordCreation(createdBy);
        }
    }

    // --- 비즈니스 메서드 ---

    // 담당 강사 변경
    public void assignTeacher(User teacher, String updatedBy) {
        this.teacher = teacher;
        recordModification(updatedBy);
    }

    // 수강 상태 변경 (활성 / 비활성)
    public void updateActiveStatus(boolean isActive, String updatedBy) {
        this.isActive = isActive;
        recordModification(updatedBy);
    }

    // 프로필 정보 수정 (학교, 학년, 본인/학부모 연락처, 메모)
    public void updateProfile(String schoolName, GradeLevel gradeLevel,
                              String parentPhoneNumber, String memo, String updatedBy) {
        this.schoolName = schoolName;
        this.gradeLevel = gradeLevel;
        this.parentPhoneNumber = parentPhoneNumber;
        this.memo = memo;
        recordModification(updatedBy);
    }
}
