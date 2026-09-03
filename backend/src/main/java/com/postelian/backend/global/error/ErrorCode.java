package com.postelian.backend.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid input value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method not allowed"),
    ENTITY_NOT_FOUND(404, "C003", "Entity not found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid type value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is denied"),

    // User
    USER_NOT_FOUND(404, "U001", "User not found"),
    EMAIL_DUPLICATION(400, "U002", "Email is duplicated"),

    // Student
    STUDENT_NOT_FOUND(404, "S001", "Student profile not found");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
