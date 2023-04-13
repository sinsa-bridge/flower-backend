package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.presentation.exception.UserAlreadyExistsException;
import io.sinsabridge.backend.presentation.exception.UserNotFoundException;
import io.sinsabridge.backend.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;

    // 사용자 가입 메서드
    public void signUp(User user, String code) {
        String phoneNumber = user.getPhoneNumber();
        boolean isVerified = smsService.verifySmsCode(phoneNumber, code);

        if (isVerified) {
            User savedUser = userRepository.save(user);
            smsService.updateSmsVerification(savedUser);
        } else {
            throw new IllegalArgumentException("SMS 인증 실패");
        }
    }

    // 사용자 등록 메서드
    public void registerUser(User userDto) {

        // 중복 사용자 확인
        checkIfUserExists(userDto.getPhoneNumber());


        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
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
                //.smsVerificationTimestamp(userDto.getSmsVerificationTimestamp())
                .smsVerificationTimestamp(LocalDateTime.now()) // 현재 시간으로 설정

                .build();
        userRepository.save(user);
    }

    private void checkIfUserExists(String phoneNumber) {
        userRepository.findByPhoneNumber(phoneNumber).ifPresent(user -> {
            throw new UserAlreadyExistsException("중복된 전화번호가 존재합니다.");
        });
    }


    // SMS 인증 업데이트 메서드
    public void updateSmsVerification(User user) {
        User foundUser = userRepository.findByPhoneNumber(user.getPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        foundUser.setSmsVerified(true);
        userRepository.save(foundUser);
    }

    // 사용자 ID로 조회하는 메서드
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다.")));
    }

    // 사용자 정보를 업데이트하는 메서드
    public User updateUser(Long id, User userUpdates) {
        Optional<User> user = findById(id);
        // 필요한 필드들을 업데이트하세요.
        // 예시: user.setPhoneNumber(userUpdates.getPhoneNumber());
        if (user.isPresent()) {
        userRepository.save(user.get());

        }
        return user.get();
    }

    // 사용자 삭제 메서드
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 전체 사용자 조회 및 페이징 처리 메서드
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

}