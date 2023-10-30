package com.example.basic.global.exception;

import com.example.basic.global.dto.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final String result;
    private final ErrorCode errorCode;
    private final String  message;

    public CustomException(ErrorCode errorCode) {
        this.result = "ERROR";
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

}
