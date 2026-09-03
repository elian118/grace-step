package com.postelian.backend.domain.user.entity;

import com.postelian.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_role", columnList = "role")
})
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String password; // 소셜 로그인 전용일 경우 null 허용 가능

    @Column(length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Builder
    public User(String name, String email, String password, String phoneNumber, Role role, UserStatus status, String createdBy) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role != null ? role : Role.STUDENT;
        this.status = status != null ? status : UserStatus.PENDING;
        if (createdBy != null) {
            recordCreation(createdBy);
        }
    }

    // 정보 수정/권한 변경용 비즈니스 메서드
    public void updateRole(Role newRole, String updatedBy) {
        this.role = newRole;
        recordModification(updatedBy);
    }

    public void updateStatus(UserStatus newStatus, String updatedBy) {
        this.status = newStatus;
        recordModification(updatedBy);
    }

    public void updateProfile(String name, String phoneNumber, String updatedBy) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        recordModification(updatedBy);
    }
}
