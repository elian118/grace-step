package com.postelian.backend.global.error.exception;

import com.postelian.backend.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
