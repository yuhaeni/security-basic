package com.example.basic.provider;

import com.example.basic.users.dto.UserSignUpDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.ref.SoftReference;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


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

    /**
     * Authentication 객체의 권한 정보를 이용해 토큰 생성
     * @param authentication - Authentication 객체
     * @return - 토큰
     */

    public String createToken(Authentication authentication) {

        // 권한 값을 받아 하나의 문자열로 합침
        String authorities = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())   // 토큰의 용도를 명시. 인증(Authentication) 객체에서 사용자 이름을 가져와서 주제로 설정.
                .claim(AUTHORITIES_KEY, authorities) // JWT의 페이로드 섹션에 사용자의 권한 정보를 설정
                .signWith(key ,SignatureAlgorithm.HS512)  // JWT 서명하기 위해 사용되는 알고리즘 및 비밀 키 설정
                .setExpiration(validity)    // 토큰 만료 시간 설정
                .compact(); // 토큰 생성 (JWT를 문자열로 직렬화)

    }

    /**
     * 토큰에서 인증 정보 조회 후 Authentication 객체 리턴
     * @param token
     * @return
     */
    //토큰 -> 클레임 추출 -> 유저 객체 제작 -> Authentication 객체 리턴
    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 파싱할 JWT의 서명을 확인하기 위해 사용할 키를 설정
                .build() // JWT 파서를 빌드하여 인스턴스 생성
                .parseClaimsJws(token) //  JWT의 서명을 확인하고 토큰을 파싱
                .getBody(); //  JWT의 페이로드 부분을 추출

        List<? extends SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")) // JWT의 페이로드에서 권한 정보를 추출
                                                            .map(SimpleGrantedAuthority::new) //
                                                            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal ,token ,authorities);

    }

}
