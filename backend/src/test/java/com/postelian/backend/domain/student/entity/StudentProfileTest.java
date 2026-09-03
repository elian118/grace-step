package com.postelian.backend.domain.student.entity;

import com.postelian.backend.domain.user.entity.Role;
import com.postelian.backend.domain.user.entity.User;
import com.postelian.backend.domain.user.entity.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudentProfileTest {

    @Test
    @DisplayName("학생 프로필 생성 시 등록자 정보가 기록되어야 한다")
    void creationTest() {
        // given
        User user = User.builder().name("Student").email("student@test.com").build();
        String userId = "teacher_01";
        
        StudentProfile profile = StudentProfile.builder()
                .user(user)
                .isActive(true)
                .createdBy(userId)
                .build();

        // then
        assertThat(profile.getCreatedBy()).isEqualTo(userId);
        assertThat(profile.getUpdatedBy()).isEqualTo(userId);
        assertThat(profile.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("프로필 수정 시 수정자 정보가 기록되어야 한다")
    void updateTest() {
        // given
        User user = User.builder().name("Student").email("student@test.com").build();
        StudentProfile profile = StudentProfile.builder()
                .user(user)
                .isActive(true)
                .createdBy("teacher_01")
                .build();

        // when
        String updateUserId = "teacher_02";
        profile.updateProfile("New School", GradeLevel.ELEM_5, "010-1111-1111", null, "메모", updateUserId);

        // then
        assertThat(profile.getSchoolName()).isEqualTo("New School");
        assertThat(profile.getUpdatedBy()).isEqualTo(updateUserId);
        assertThat(profile.getCreatedBy()).isEqualTo("teacher_01");
    }
}
