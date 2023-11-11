package com.example.basic.member.service;

import com.example.basic.global.dto.ErrorCode;
import com.example.basic.global.dto.TokenInfoDto;
import com.example.basic.global.exception.CustomException;
import com.example.basic.global.util.SecurityUtil;
import com.example.basic.jwt.JwtTokenProvider;
import com.example.basic.member.domain.Authority;
import com.example.basic.member.domain.Member;
import com.example.basic.member.domain.MemberRepository;
import com.example.basic.member.dto.LoginRequestMemberDto;
import com.example.basic.member.dto.SignRequestMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManager;

    @Transactional
    public void signUp (SignRequestMemberDto dto) throws Exception {
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_USER_EMAIL);
        }

        String password = passwordEncoder.encode(dto.getPassword()); // 비밀번호 암호화
        Member member = dto.toEntity(password,Authority.ROLE_USER);

        memberRepository.save(member);
    }

    @Transactional
    public TokenInfoDto login(LoginRequestMemberDto dto) {

        // 1. AuthenticationManager를 통해 인증을 시도하고 인증이 성공하면 Authentication 객체를 리턴받는다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        // 1-1. authenticationToken을 이용해서  Authentication 객체를 생성하려고 authenticate() 메소드가 실행될 때,  CustomUserDetailsService의 loadUserByUsername() 메소드가 실행
        Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);

        // 2. SecurityContextHolder에 위에서 생성한 Authentication 객체를 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. JwtTokenProvider를 통해 JWT 토큰을 생성 후 리턴
        return jwtTokenProvider.createToken(authentication);

    }

    @Transactional(readOnly = true)
    public Optional<Member> getUserWithAuthorities(String email) {
        // email을 기준으로 정보를 가져온다.
        return memberRepository.findByEmail(email);
    }
    @Transactional(readOnly = true)
    public Optional<Member> getMyUserWithAuthorities() {
        // Security Context에 저장된 username의 정보만 가져온다.
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findByEmail);
    }

}
