package com.example.basic.users.controller;

import com.example.basic.global.dto.TokenInfoDto;
import com.example.basic.users.dto.RequestUserDto;
import com.example.basic.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/sign-up")
    public String singUp (@RequestBody RequestUserDto dto) throws Exception {
        userService.signUp(dto);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public TokenInfoDto login(@RequestBody RequestUserDto requestUserDto){
        return userService.login(requestUserDto);
    }

}
