package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.presentation.dto.UserDto;
import io.sinsabridge.backend.presentation.exception.UserAlreadyExistsException;
import io.sinsabridge.backend.presentation.exception.UserNotFoundException;
import io.sinsabridge.backend.sms.domain.repository.SmsHistoryRepository;
import io.sinsabridge.backend.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private final SmsHistoryRepository smsHistoryRepository;


    // 문자 메시지 인증 코드 확인
    public boolean verifySmsCode(String phoneNumber, String verificationCode) {

        // 인증 코드를 받아서 smshistory를 뒤진 후 있으면 true
        // 없으면 false

        return smsService.verifySmsCode(phoneNumber, verificationCode);
    }


    // 사용자 가입 메서드
    public void signUp(User user, String code) {
        // 널 체크
        Objects.requireNonNull(user, "사용자 정보가 없습니다.");
        Objects.requireNonNull(code, "인증 코드가 없습니다.");

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
    public UserDto registerUser(UserDto userDto) {
        // 널 체크
        Objects.requireNonNull(userDto, "사용자 정보가 없습니다.");

        // 중복 사용자 확인
        checkIfUserExists(userDto.getPhoneNumber());

        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = User.builder()
                .phoneNumber(userDto.getPhoneNumber())
                .password(encryptedPassword)
                .gender(userDto.getGender())
                .region(userDto.getRegion())
                .smsVerificationTimestamp(LocalDateTime.now()) //현재 시간으로 설정
                .build();
        userRepository.save(user);
        return userDto;
    }

    private void checkIfUserExists(String phoneNumber) {
        // 널 체크
        Objects.requireNonNull(phoneNumber, "전화번호가 없습니다.");

        userRepository.findByPhoneNumber(phoneNumber).ifPresent(user -> {
            throw new UserAlreadyExistsException("중복된 전화번호가 존재합니다.");
        });
    }

    // SMS 인증 업데이트 메서드
    public void updateSmsVerification(User user) {
        // 널 체크
        Objects.requireNonNull(user, "사용자 정보가 없습니다.");

        User foundUser = userRepository.findByPhoneNumber(user.getPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        foundUser.setSmsVerified(true);
        userRepository.save(foundUser);
    }

    // 사용자 ID로 조회하는 메서드
    public Optional<User> findById(Long id) {
        // 널 체크
        Objects.requireNonNull(id, "사용자 ID가 없습니다.");

        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다.")));
    }

    // 사용자 정보를 업데이트하는 메서드
    public User updateUser(Long id, UserDto userUpdates) {
        // 널 체크
        Objects.requireNonNull(id, "사용자 ID가 없습니다.");
        Objects.requireNonNull(userUpdates, "사용자 정보가 없습니다.");

        Optional<User> user = findById(id);


        if (user.isPresent()) {
            userRepository.save(user.get());
        }
        return user.get();
    }

    // 사용자 삭제 메서드
    public void deleteUser(Long id) {
        // 널 체크
        Objects.requireNonNull(id, "사용자 ID가 없습니다.");

        userRepository.deleteById(id);
    }

    // 전체 사용자 조회 및 페이징 처리 메서드
    public Page<User> findAllUsers(Pageable pageable) {
        // 널 체크
        Objects.requireNonNull(pageable, "페이징 정보가 없습니다.");

        return userRepository.findAll(pageable);
    }
}
