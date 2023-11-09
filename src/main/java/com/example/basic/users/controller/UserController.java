package com.example.basic.users.controller;

import com.example.basic.global.dto.TokenInfoDto;
import com.example.basic.users.dto.LoginRequestMemberDto;
import com.example.basic.users.dto.SignRequestMemberDto;
import com.example.basic.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<String> singUp (@RequestBody @Valid SignRequestMemberDto dto , Errors errors) throws Exception {
        userService.signUp(dto);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public TokenInfoDto login(@RequestBody @Valid LoginRequestMemberDto loginRequestMemberDto){
        return userService.login(loginRequestMemberDto);
    }

}
