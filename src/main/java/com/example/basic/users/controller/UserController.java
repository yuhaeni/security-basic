package com.example.basic.users.controller;

import com.example.basic.global.dto.ErrorCode;
import com.example.basic.global.dto.TokenInfoDto;
import com.example.basic.global.exception.CustomException;
import com.example.basic.users.domain.User;
import com.example.basic.users.dto.LoginRequestUserDto;
import com.example.basic.users.dto.SignRequestUserDto;
import com.example.basic.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
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

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        Optional<User> user = userService.getMyUserWithAuthorities();

        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new CustomException(ErrorCode.USERNAME_NOT_FOUND);
        }

    }
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        Optional<User> user = userService.getUserWithAuthorities(username);

        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new CustomException(ErrorCode.USERNAME_NOT_FOUND);
        }
    }





}
