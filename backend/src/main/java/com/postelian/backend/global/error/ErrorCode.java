package com.postelian.backend.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "허용되지 않는 메소드입니다."),
    ENTITY_NOT_FOUND(404, "C003", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 오류입니다."),
    INVALID_TYPE_VALUE(400, "C005", "잘못된 유형의 값입니다."),
    HANDLE_ACCESS_DENIED(403, "C006", "접근이 거부되었습니다."),

    // User
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    EMAIL_DUPLICATION(400, "U002", "이미 존재하는 이메일입니다."),

    // Student
    STUDENT_NOT_FOUND(404, "S001", "학생 프로필을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
