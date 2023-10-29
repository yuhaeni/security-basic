package com.example.basic.users.dto;

import com.example.basic.users.domain.Authority;
import com.example.basic.users.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class RequestUserDto {

    private String email;
    private String password;
    private String name;
    private int age;
    private Authority authority;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .age(age)
                .authority(authority)
                .build();
    }

    //비밀번호 암호화
    public void encryptPassword(PasswordEncoder passwordEncoder, String rawPassword) {
        this.password = passwordEncoder.encode(rawPassword);
    }
}
