package com.example.basic.member.dto;

import com.example.basic.member.domain.Authority;
import com.example.basic.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMemberDto {

    private String email;
    private String password;
    private String name;
    private int age;
    private Authority authority;

    // entity -> dto
    public ResponseMemberDto(Member entity) {
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.name = entity.getName();
        this.age = entity.getAge();
        this.authority = entity.getAuthority();
    }

}
