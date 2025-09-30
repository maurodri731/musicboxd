package com.mau.musicboxd.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),  // 409 is more appropriate than 403
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}

@Data
@AllArgsConstructor
class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}