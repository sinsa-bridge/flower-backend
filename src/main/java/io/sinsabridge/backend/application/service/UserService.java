// UserService.java
package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.SmsHistoryRepository;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.presentation.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserDto userDto) {
        String encryptedPassword = passwordEncoder.encode(userDto.getPhoneNumber());
        User user = User.builder()
                .phoneNumber(userDto.getPhoneNumber())
                .password(encryptedPassword)
                .gender(userDto.getGender())
                .birthDate(userDto.getBirthDate())
                .hobbies(userDto.getHobbies())
                .region(userDto.getRegion())
                .profileImage(userDto.getProfileImage())
                .paid(userDto.isPaid())
                .active(userDto.isActive())
                .smsVerificationTimestamp(userDto.getSmsVerificationTimestamp())
                .build();
        userRepository.save(user);
    }

    // 다른 메서드
}
