package com.example.basic.users.dto;

import com.example.basic.users.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class LoginRequestUserDto {

    private String email;
    private String password;

}
