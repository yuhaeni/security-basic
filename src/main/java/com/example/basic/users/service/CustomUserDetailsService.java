package com.example.basic.users.service;

import com.example.basic.users.domain.User;
import com.example.basic.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 시, DB에서 유저 정보와 권한 정보를 가져온다.
        // 해당 정보를 기반으로 userdetails.User 객체를 생성해서 리턴한다.
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow(()-> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .age(user.getAge())
                .authority(user.getAuthority())
                .build();
    }
}
