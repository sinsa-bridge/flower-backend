// JwtConfig.java
package io.sinsabridge.backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token-validity}")
    private long tokenValidity;

    public String getSecretKey() {
        return secretKey;
    }

    public long getTokenValidity() {
        return tokenValidity;
    }
}
