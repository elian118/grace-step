package com.postelian.backend.global.error;

import com.postelian.backend.global.error.exception.BusinessException;
import com.postelian.backend.global.error.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBusinessException_ShouldReturnCustomErrorResponse() {
        // given
        BusinessException exception = new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);

        // when
        ResponseEntity<ErrorResponse> response = handler.handleBusinessException(exception);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("U001", response.getBody().getCode());
    }

    @Test
    void handleException_ShouldReturnInternalServerErrorResponse() {
        // given
        Exception exception = new RuntimeException("Unexpected test exception");

        // when
        ResponseEntity<ErrorResponse> response = handler.handleException(exception);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Server error", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("C004", response.getBody().getCode());
    }
}
