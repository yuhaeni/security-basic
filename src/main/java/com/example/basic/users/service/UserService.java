package com.example.basic.users.service;

import com.example.basic.users.domain.User;
import com.example.basic.users.domain.UserRepository;
import com.example.basic.users.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public User signUp (UserSignUpDto dto) throws Exception {
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        dto.encryptPassword(passwordEncoder, dto.getPassword());
        return userRepository.save(dto.toEntity());

    }

}
