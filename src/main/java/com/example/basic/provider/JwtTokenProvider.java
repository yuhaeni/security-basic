package com.example.basic.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.ref.SoftReference;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;


@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secretKey;
    private final long tokenValidityInMilliseconds;
    private Key key;

    // 1.Bean 생성 후 주입 받은 후에
    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey,@Value("${jwt.token-validity-in-seconds}") Long tokenValidityInMilliseconds) {
        this.secretKey = secretKey;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }


    // 2. secret 값을 Base64로 디코딩해 Key 변수에 할당
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
