package com.postelian.backend.domain.user.controller;

import com.postelian.backend.domain.user.dto.UserResponse;
import com.postelian.backend.domain.user.dto.UserSignUpRequest;
import com.postelian.backend.domain.user.dto.UserUpdateRequest;
import com.postelian.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postelian.backend.domain.user.entity.Role;
import com.postelian.backend.global.common.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@Tag(name = "User API", description = "회원 관리 관련 API")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 다건 조회", description = "필터 조건에 따라 회원 목록을 페이지 단위로 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getUserList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Role role,
            @PageableDefault Pageable pageable) {
        PageResponse<UserResponse> response = userService.getUserList(name, email, role, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 가입", description = "새로운 회원을 가입시킵니다.")
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(
            @RequestBody @Valid UserSignUpRequest request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "system")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        UserResponse response = userService.signUp(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "회원 정보 수정", description = "기존 회원의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        UserResponse response = userService.updateUser(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴 처리(Soft Delete)합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @Parameter(description = "작업을 수행하는 사용자 아이디", example = "teacher_01")
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "system") String userId) {
        userService.deleteUser(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 단건 조회", description = "회원 ID로 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }
}
