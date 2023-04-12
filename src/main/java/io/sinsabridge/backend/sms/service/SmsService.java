// SmsService.java
package io.sinsabridge.backend.sms.service;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.sms.domain.entity.SmsVerification;
import io.sinsabridge.backend.sms.domain.repository.SmsHistoryRepository;
import io.sinsabridge.backend.sms.domain.repository.SmsVerificationRepository;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsSender smsSender;
    private final SmsHistoryRepository smsHistoryRepository;
    private final SmsVerificationRepository smsVerificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public SmsSendResponse sendVerificationCode(String phoneNumber, String ipAddress) {
        String verificationCode = generateVerificationCode();

        SmsSendRequest request = SmsSendRequest.builder()
                .receiver(phoneNumber)
                .msg("인증번호: " + verificationCode)
                .build();

        SmsSendResponse response = smsSender.send(request);

        if (response.isSuccess()) {
            SmsVerification smsVerification = new SmsVerification();
            smsVerification.setPhoneNumber(phoneNumber);
            smsVerification.setVerificationCode(verificationCode);
            smsVerification.setVerified(false);
            smsVerification.setVerificationAttempts(0);
            smsVerification.setLastAttemptTime(LocalDateTime.now());
            smsVerificationRepository.save(smsVerification);
        }

        return response;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        return String.valueOf(code);
    }

    public boolean verifySmsCode(String phoneNumber, String code) {
        SmsVerification smsVerification = smsVerificationRepository.findByPhoneNumber(phoneNumber);

        if (smsVerification != null && !smsVerification.isVerified()) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastAttemptTime = smsVerification.getLastAttemptTime();

            if (smsVerification.getVerificationAttempts() < 3 &&
                    lastAttemptTime.plusMinutes(1).isAfter(now)) {
                if (smsVerification.getVerificationCode().equals(code)) {
                    smsVerification.setVerified(true);
                    smsVerification.setVerificationTime(now);
                    smsVerificationRepository.save(smsVerification);
                    return true;
                } else {
                    smsVerification.setVerificationAttempts(smsVerification.getVerificationAttempts() + 1);
                    smsVerification.setLastAttemptTime(now);
                    smsVerificationRepository.save(smsVerification);

                    if (smsVerification.getVerificationAttempts() >= 3) {
                        smsVerification.setLastAttemptTime(now.plusMinutes(5));
                        smsVerificationRepository.save(smsVerification);
                    }
                }
            }
        }
        return false;
    }

    @Transactional
    public void updateSmsVerification(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            User savedUser = optionalUser.get();
            savedUser.setSmsVerified(true);
            userRepository.save(savedUser);
        } else {
            throw new IllegalStateException("User not found in the UserRepository.");
        }
    }
}
