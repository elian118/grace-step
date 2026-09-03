package com.postelian.backend.domain.student.dto;

import com.postelian.backend.domain.student.entity.GradeLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentProfileDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "학생 프로필 등록/수정 요청 DTO")
    public static class StudentProfileRequest {

        @NotNull
        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @NotNull
        @Schema(description = "담당 강사 ID", example = "2")
        private Long teacherId;

        @NotBlank
        @Schema(description = "학교명", example = "광명중학교")
        private String schoolName;

        @NotNull
        @Schema(description = "학년", example = "MIDDLE_1")
        private GradeLevel gradeLevel;

        @Schema(description = "학생 본인 연락처", example = "010-1111-2222")
        private String phoneNumber;

        @Schema(description = "학부모 연락처", example = "010-3333-4444")
        private String parentPhoneNumber;

        @Schema(description = "메모", example = "기초 문법 보충 필요")
        private String memo;

        @Builder
        public StudentProfileRequest(Long userId, Long teacherId, String schoolName, GradeLevel gradeLevel,
                                     String phoneNumber, String parentPhoneNumber, String memo) {
            this.userId = userId;
            this.teacherId = teacherId;
            this.schoolName = schoolName;
            this.gradeLevel = gradeLevel;
            this.phoneNumber = phoneNumber;
            this.parentPhoneNumber = parentPhoneNumber;
            this.memo = memo;
        }
    }

    @Getter
    @Schema(description = "학생 프로필 응답 DTO")
    public static class StudentProfileResponse {

        @Schema(description = "학생 프로필 ID")
        private final Long id;

        @Schema(description = "학생 이름")
        private final String studentName;

        @Schema(description = "담당 강사 이름")
        private final String teacherName;

        @Schema(description = "학교명")
        private final String schoolName;

        @Schema(description = "학년")
        private final GradeLevel gradeLevel;

        @Schema(description = "활성 여부")
        private final Boolean isActive;

        public StudentProfileResponse(com.postelian.backend.domain.student.entity.StudentProfile profile) {
            this.id = profile.getId();
            this.studentName = profile.getUser().getName();
            this.teacherName = profile.getTeacher() != null ? profile.getTeacher().getName() : null;
            this.schoolName = profile.getSchoolName();
            this.gradeLevel = profile.getGradeLevel();
            this.isActive = profile.getIsActive();
        }

        public static StudentProfileResponse from(com.postelian.backend.domain.student.entity.StudentProfile profile) {
            return new StudentProfileResponse(profile);
        }
    }
}
