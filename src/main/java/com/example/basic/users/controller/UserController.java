package com.example.basic.users.controller;

import com.example.basic.global.dto.TokenInfoDto;
import com.example.basic.users.dto.LoginRequestUserDto;
import com.example.basic.users.dto.SignRequestUserDto;
import com.example.basic.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<String> singUp (@RequestBody @Valid SignRequestUserDto dto , Errors errors) throws Exception {
        userService.signUp(dto);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public TokenInfoDto login(@RequestBody @Valid LoginRequestUserDto loginRequestUserDto){
        return userService.login(loginRequestUserDto);
    }

}
