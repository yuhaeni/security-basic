package com.example.basic.global.util;

import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.swing.*;
import java.util.Optional;

@NoArgsConstructor
public class SecurityUtil {

    public static Optional<String> getCurrentUsername() {

        // Security Context의 Authentication 객체를 이용해 username 반환한다.
        // JwtAuthenticationFilter의 doFilter 메소드에서 요청이 들어올 때, Security Context의 Authentication 객체를 저장해서 사용한다.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null) {
            // Security에 Context 인증 정보가 없다.
            return Optional.empty();
        }

        String username = null;

        if(authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = String.valueOf(authentication.getPrincipal().toString());
        }

        return Optional.ofNullable(username);

    }

}
