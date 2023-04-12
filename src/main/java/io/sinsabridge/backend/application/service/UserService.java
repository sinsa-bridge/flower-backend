// UserService.java
package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            smsService.updateSmsVerification(savedUser);
        } else {
            throw new IllegalArgumentException("SMS verification failed");
        }
    }

    public void registerUser(User userDto) {
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

    public void updateSmsVerification(User user) {
        User foundUser = userRepository.findByPhoneNumber(user.getPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        foundUser.setSmsVerified(true);
        userRepository.save(foundUser);
    }

    // 사용자 ID로 조회하는 메서드 추가
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // 사용자 정보를 업데이트하는 메서드 추가
    public User updateUser(Long id, User userUpdates) {
        User user = findById(id);
        // 필요한 필드들을 업데이트하세요.
        // 예시: user.setPhoneNumber(userUpdates.getPhoneNumber());
        userRepository.save(user);
        return user;
    }

    // 사용자 삭제 메서드 추가
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 전체 사용자 조회 및 페이징 처리 메서드 추가
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
