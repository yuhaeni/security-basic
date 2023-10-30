package com.example.basic.global.config;

import com.example.basic.jwt.JwtAuthenticationFilter;
import com.example.basic.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // JWT를 사용하기 위해서는 기본적으로 password encoder가 필요
    // BCryptPasswordEncoder 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // basic auth 및 csrf 보안을 사용하지 않는다
                .httpBasic().disable()
                .csrf().disable()
                // JWT를 사용하기 때문에 세션을 사용하지 않는다
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 요청들에 대한 접근제한을 설정한다
                .authorizeHttpRequests()
                // 해당 요청에 대해서는 모든 요청을 허가한다
                .antMatchers("/sign-up").permitAll()
                // 나머지 요청에 대해서는 모두 인증되어야 한다
                .anyRequest().authenticated()
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행한다.
                .and()
                // Spring Security에 내장된 UsernamePasswordAuthenticationFilter 필터 앞에 JwtAuthenticationFilter를 추가하겠다.
                // HTTP 요청이 도착할 때 JwtAuthenticationFilter가 먼저 실행되고, 그 후 UsernamePasswordAuthenticationFilter가 실행된다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider) , UsernamePasswordAuthenticationFilter.class)
                ;
        return httpSecurity.build();
    }


}
