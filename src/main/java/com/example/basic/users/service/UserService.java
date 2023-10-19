package com.example.basic.users.service;

import com.example.basic.global.dto.ResponseTokenDto;
import com.example.basic.global.filter.JwtFilter;
import com.example.basic.provider.JwtTokenProvider;
import com.example.basic.users.domain.User;
import com.example.basic.users.domain.UserRepository;
import com.example.basic.users.dto.RequestUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManager;

    public User signUp (RequestUserDto dto) throws Exception {
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        dto.encryptPassword(passwordEncoder, dto.getPassword());
        return userRepository.save(dto.toEntity());

    }

    public ResponseEntity<ResponseTokenDto> login(RequestUserDto dto) {

        // 1. AuthenticationManager를 통해 인증을 시도하고 인증이 성공하면 Authentication 객체를 리턴받는다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        // UserDetailsServiceImpl의 loadUserByUsername() 메소드가 실행
        Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);

        // 2. SecurityContextHolder에 위에서 생성한 Authentication 객체를 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 3. JwtTokenProvider를 통해 JWT 토큰을 생성한다.
        String jwtToken = jwtTokenProvider.createToken(authentication);

        // 4. 생성한 JWT 토큰을 Response Header에 담아서 리턴한다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtToken);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(new ResponseTokenDto(jwtToken));

    }



}
