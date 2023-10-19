package com.example.basic.users.controller;

import com.example.basic.users.dto.RequestUserDto;
import com.example.basic.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("sign-up")
    public String singUp (@RequestBody RequestUserDto dto) throws Exception {
        userService.signUp(dto);
        return "회원가입 성공";
    }

}
