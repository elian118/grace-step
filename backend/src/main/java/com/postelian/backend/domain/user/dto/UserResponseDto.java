package com.postelian.backend.domain.user.dto;

import com.postelian.backend.domain.user.entity.Role;
import com.postelian.backend.domain.user.entity.User;
import com.postelian.backend.domain.user.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원 정보 응답 DTO")
public class UserResponseDto {

    @Schema(description = "ID")
    private final Long id;

    @Schema(description = "이름")
    private final String name;

    @Schema(description = "이메일")
    private final String email;

    @Schema(description = "연락처")
    private final String phoneNumber;

    @Schema(description = "역할")
    private final Role role;

    @Schema(description = "상태")
    private final UserStatus status;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
        this.status = user.getStatus();
    }

    // id getter check: User.java has @Getter so user.getId() should work.
    // In User.java: private Long id; so user.getId()
    public static UserResponseDto from(User user) {
        return new UserResponseDto(user);
    }

    private UserResponseDto(User user, boolean internal) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
        this.status = user.getStatus();
    }

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user, true);
    }
}
