package io.sinsabridge.backend.presentation.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* io.sinsabridge.backend.presentation.controller.*.*(..))")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            logger.info("User: {} - {}#{}", username, joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        } else {
            logger.info("비로그인 사용자 - {}#{}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        }
    }
}
