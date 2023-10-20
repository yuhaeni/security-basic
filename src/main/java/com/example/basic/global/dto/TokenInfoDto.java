package com.example.basic.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfoDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
