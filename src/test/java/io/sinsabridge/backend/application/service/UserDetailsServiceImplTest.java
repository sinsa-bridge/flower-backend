package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Log
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

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
    }

    @Test
    void testLoadUserByUsername() {
        // Given
        when(userRepository.findByPhoneNumber(testUser.getPhoneNumber())).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getPhoneNumber());


        // Then
        assertEquals(testUser.getPhoneNumber(), userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());

        log.log(Level.INFO, testUser.toString());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        // Given
        when(userRepository.findByPhoneNumber(testUser.getPhoneNumber())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(testUser.getPhoneNumber()));
    }
}
