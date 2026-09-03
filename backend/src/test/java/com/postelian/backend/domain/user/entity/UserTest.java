package com.postelian.backend.domain.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("사용자 엔티티 생성 시 등록자 정보가 기록되어야 한다")
    void creationTest() {
        // given
        String userId = "admin_01";
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .role(Role.STUDENT)
                .status(UserStatus.ACTIVE)
                .createdBy(userId)
                .build();

        // then
        assertThat(user.getCreatedBy()).isEqualTo(userId);
        assertThat(user.getUpdatedBy()).isEqualTo(userId);
        assertThat(user.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("정보 수정 시 수정자 정보가 기록되어야 한다")
    void updateTest() {
        // given
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .role(Role.STUDENT)
                .status(UserStatus.ACTIVE)
                .createdBy("admin_01")
                .build();

        // when
        String updateUserId = "admin_02";
        user.updateProfile("Updated Name", "010-1234-5678", updateUserId);

        // then
        assertThat(user.getName()).isEqualTo("Updated Name");
        assertThat(user.getUpdatedBy()).isEqualTo(updateUserId);
        assertThat(user.getCreatedBy()).isEqualTo("admin_01");
    }
}
