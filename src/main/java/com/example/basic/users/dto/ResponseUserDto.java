package com.example.basic.users.dto;

import com.example.basic.users.domain.Authority;
import com.example.basic.users.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseUserDto {

    private String email;
    private String password;
    private String name;
    private int age;
    private Authority authority;

    // entity -> dto
    public ResponseUserDto(User entity) {
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.name = entity.getName();
        this.age = entity.getAge();
        this.authority = entity.getAuthority();
    }

}
