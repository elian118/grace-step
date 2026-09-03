package com.postelian.backend.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원 정보 수정 요청 DTO")
public class UserUpdateRequest {

    @NotBlank
    @Schema(description = "이름", example = "홍길순")
    private String name;

    @Schema(description = "연락처", example = "010-8765-4321")
    private String phoneNumber;

    @Builder
    public UserUpdateRequest(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
