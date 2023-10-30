package com.example.basic.provider;

import com.example.basic.global.dto.TokenInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth"; // JWT 토큰의 클레임 내부에서 사용자의 권한
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

    public TokenInfoDto createToken(Authentication authentication) {

        // 권한 값을 받아 하나의 문자열로 합침
        String authorities = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

        // 토큰 만료 시간 설정
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + this.tokenValidityInMilliseconds);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000)) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // JWT 서명하기 위해 사용되는 알고리즘 및 비밀 키 설정
                .compact(); // 토큰 생성 (JWT를 문자열로 직렬화)

        return TokenInfoDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

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

    /**
     * 필터에서 사용할 토큰 검증
     * @param token 필터 정보
     * @return 토큰이 유효 여부
     */

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
           // 잘못된 JWT 서명
        } catch (ExpiredJwtException e) {
           // 만료된 JWT 토큰
        } catch (UnsupportedJwtException e) {
           // 지원되지 않는 JWT 토큰
        } catch (IllegalArgumentException e) {
            // 잘못된 JWT 토큰
        }
        return false;


    }

}
