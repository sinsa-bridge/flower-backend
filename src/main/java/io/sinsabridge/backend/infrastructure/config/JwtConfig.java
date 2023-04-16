package io.sinsabridge.backend.infrastructure.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Configuration
public class JwtConfig {
    @Value("${security.jwt.secret-key}")
    private String secretKey;  // WT의 비밀키 값

    @Value("${security.jwt.token-validity}")
    private long tokenValidity;  //JWT의 토큰 유효 기간

    @Value("${security.jwt.header-string}")
    private String headerString;  // JWT를 포함하는 HTTP 요청 헤더에 사용될 문자열

    @Value("${security.jwt.token-prefix}")
    private String tokenPrefix;  // JWT 토큰 값의 접두사

    /**
     * JWT 비밀 키를 가져옵니다.
     *
     * @return 비밀 키
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * JWT 토큰 유효 기간을 가져옵니다.
     *
     * @return 토큰 유효 기간 (밀리 초)
     */
    public long getTokenValidity() {
        return tokenValidity;
    }

    /**
     * JWT 헤더 문자열을 가져옵니다.
     *
     * @return 헤더 문자열
     */
    public String getHeaderString() {
        return headerString;
    }

    /**
     * JWT 토큰 접두사를 가져옵니다.
     *
     * @return 토큰 접두사
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * JWT 비밀 키를 바이트 배열로 가져옵니다.
     *
     * @return 비밀 키의 바이트 배열
     */
    public byte[] getSecret() {
        return secretKey.getBytes();
    }

    /**
     * JWT 토큰 만료 시간을 가져옵니다.
     *
     * @return 토큰 만료 시간 (밀리 초)
     */
    public long getExpirationTime() {
        return tokenValidity;
    }

    public String generateToken(Authentication authentication) {
        String phoneNumber = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        return JWT.create()
                .withSubject(phoneNumber)
                .withExpiresAt(new Date(System.currentTimeMillis() + getExpirationTime()))
                .sign(Algorithm.HMAC512(getSecret()));
    }
}
