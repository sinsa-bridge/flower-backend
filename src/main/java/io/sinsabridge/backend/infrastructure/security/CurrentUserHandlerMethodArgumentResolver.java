package io.sinsabridge.backend.infrastructure.security;

import io.sinsabridge.backend.application.service.UserDetailsServiceImpl;
import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.infrastructure.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@ControllerAdvice
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CurrentUserHandlerMethodArgumentResolver(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null &&
                parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Authentication authentication = (Authentication) request.getUserPrincipal();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication instanceof PreAuthenticatedAuthenticationToken)) {
            throw new UsernameNotFoundException("Cannot retrieve user details");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        if (userDetails == null || !(userDetails instanceof User)) {
            throw new UsernameNotFoundException("Cannot retrieve user details");
        }

        return (User) userDetails;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this);
    }
}
