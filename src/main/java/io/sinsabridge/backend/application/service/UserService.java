// UserService.java
package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.presentation.dto.UserDto;
import io.sinsabridge.backend.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;

    public void signUp(User user, String code) {
        String phoneNumber = user.getPhoneNumber();
        boolean isVerified = smsService.verifySmsCode(phoneNumber, code);

        if (isVerified) {
            User savedUser = userRepository.save(user);
          //  smsService.updateSmsVerification(savedUser);
        } else {
            throw new IllegalArgumentException("SMS verification failed");
        }
    }

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
