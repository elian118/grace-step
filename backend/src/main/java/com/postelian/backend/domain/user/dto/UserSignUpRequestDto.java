package com.postelian.backend.domain.user.dto;

import com.postelian.backend.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원 가입 요청 DTO")
public class UserSignUpRequestDto {

    @NotBlank
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank
    @Email
    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", example = "password123")
    private String password;

    @Schema(description = "연락처", example = "010-1234-5678")
    private String phoneNumber;

    @NotNull
    @Schema(description = "역할 (ADMIN, TEACHER, STAFF, STUDENT)", example = "STUDENT")
    private Role role;

    @Builder
    public UserSignUpRequestDto(String name, String email, String password, String phoneNumber, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
