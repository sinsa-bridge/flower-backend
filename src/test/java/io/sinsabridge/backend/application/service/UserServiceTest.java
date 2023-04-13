package io.sinsabridge.backend.application.service;// UserServiceTest.java

import io.sinsabridge.backend.application.service.UserService;
import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .phoneNumber("01012345678")
                .password("password")
                .gender("male")
                .hobbies("coding")
                .region("Seoul")
                .profileImage("image.jpg")
                .paid(false)
                .active(true)
                .smsVerified(false)
                .smsVerificationTimestamp(LocalDateTime.now())
                .build();

      //  when(userRepository.save(testUser)).thenReturn(testUser);
       // when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

    }

    // 회원 가입 테스트
    @Test
    void testRegisterUser() {
        // given
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        userService.registerUser(testUser);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }


    // SMS 인증 상태 업데이트 테스트
    @Test
    void testUpdateSmsVerification() {
        // given
        when(userRepository.findByPhoneNumber(testUser.getPhoneNumber())).thenReturn(Optional.of(testUser));

        // when
        userService.updateSmsVerification(testUser);

        // then
        verify(userRepository, times(1)).findByPhoneNumber(testUser.getPhoneNumber());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdateSmsVerification2() {
        // given
        when(userRepository.findByPhoneNumber(testUser.getPhoneNumber())).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        // when
        userService.updateSmsVerification(testUser);

        // then
        verify(userRepository, times(1)).findByPhoneNumber(testUser.getPhoneNumber());
        verify(userRepository, times(1)).save(testUser);
    }
}
