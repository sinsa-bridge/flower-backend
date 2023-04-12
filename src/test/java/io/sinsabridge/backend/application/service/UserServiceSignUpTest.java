package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.sms.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

public class UserServiceSignUpTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private UserService userService;

    private User user;
    private String code;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setPhoneNumber("01012345678");
        user.setPassword("password123");

        code = "1234";
    }

    @Test
    void signUp_success() {
        // Given
        when(smsService.verifySmsCode(user.getPhoneNumber(), code)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        userService.signUp(user, code);

        // Then
        verify(smsService, times(1)).verifySmsCode(user.getPhoneNumber(), code);
        verify(userRepository, times(1)).save(any(User.class));
        verify(smsService, times(1)).updateSmsVerification(any(User.class));
    }

    @Test
    void signUp_failed() {
        // Given
        when(smsService.verifySmsCode(user.getPhoneNumber(), code)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.signUp(user, code));
        verify(smsService, times(1)).verifySmsCode(user.getPhoneNumber(), code);
        verify(userRepository, times(0)).save(any(User.class));
        verify(smsService, times(0)).updateSmsVerification(any(User.class));
    }
}