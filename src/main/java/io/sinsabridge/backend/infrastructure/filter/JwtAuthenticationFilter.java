// JwtAuthenticationFilter.java
package io.sinsabridge.backend.infrastructure.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.sinsabridge.backend.infrastructure.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtConfig jwtConfig;

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String phoneNumber = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        String token = JWT.create().withSubject(phoneNumber).withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getTokenValidity())).sign(algorithm);
        response.addHeader("Authorization", "Bearer " + token);
    }
}
