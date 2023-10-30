package com.example.basic.global.controller;

import com.example.basic.global.dto.ExceptionDto;
import com.example.basic.global.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleRuntimeException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(new ExceptionDto(ex.getErrorCode()));
    }
}
