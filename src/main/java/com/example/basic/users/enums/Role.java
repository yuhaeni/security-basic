package com.example.basic.users.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    AGUEST("ROLE_GUEST")
    ,USER("ROLE_USER");

    private final String key;
}
