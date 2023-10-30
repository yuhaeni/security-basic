package com.example.basic.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
    ,INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다.")
    ,DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다.")
    ;

    private final HttpStatus status;
    private final String message;

}
