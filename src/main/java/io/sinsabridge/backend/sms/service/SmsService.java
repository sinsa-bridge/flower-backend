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
        String verificationCode = generateVerificationCode(); // 인증 코드 생성해

        SmsSendRequest request = SmsSendRequest.builder()
                .receiver(phoneNumber)
                .msg("인증번호: " + verificationCode)
                .build();

        SmsSendResponse response = smsSender.send(request); // 문자 보내기

        if (response.isSuccess()) {
            smsHistoryRepository.save(response.toSmsHistory(phoneNumber)); // 문자 발송 기록 저장해
            SmsVerification smsVerification = new SmsVerification(); // 인증 정보 객체 만들어
            smsVerification.setPhoneNumber(phoneNumber); // 전화번호 저장해
            smsVerification.setVerificationCode(verificationCode); // 인증 코드 저장해
            smsVerification.setVerified(false); // 인증 상태는 아직 안 된 걸로 해
            smsVerification.setVerificationAttempts(0); // 시도 횟수 초기화해
            smsVerification.setLastAttemptTime(LocalDateTime.now()); // 시도 시간 저장해
            smsVerificationRepository.save(smsVerification); // 인증 정보 저장해
        }

        return response;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        return String.valueOf(code);
    }

    public boolean verifySmsCode(String phoneNumber, String code) {
        SmsVerification smsVerification = smsVerificationRepository.findByPhoneNumber(phoneNumber); // 전화번호로 인증 정보 가져와

        if (smsVerification != null && !smsVerification.isVerified()) { // 인증 정보가 있고, 아직 인증이 안 됐으면
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastAttemptTime = smsVerification.getLastAttemptTime();

            if (smsVerification.getVerificationAttempts() < 3 && // 시도 횟수가 3회 미만이고
                    lastAttemptTime.plusMinutes(1).isAfter(now)) { // 마지막 시도로부터 1분이 지나지 않았으면
                if (smsVerification.getVerificationCode().equals(code)) { // 인증 코드가 맞으면
                    smsVerification.setVerified(true); // 인증 상태를 완료로 변경해
                    smsVerification.setVerificationTime(now); // 인증 시간 저장해
                    smsVerificationRepository.save(smsVerification); // 인증 정보 저장해
                    return true;
                } else { // 인증 코드가 틀리면
                    smsVerification.setVerificationAttempts(smsVerification.getVerificationAttempts() + 1); // 시도 횟수 증가시켜
                    smsVerification.setLastAttemptTime(now); // 시도 시간 업데이트해
                    smsVerificationRepository.save(smsVerification); // 인증 정보 저장해

                    if (smsVerification.getVerificationAttempts() >= 3) { // 시도 횟수가 3회 이상이면
                        smsVerification.setLastAttemptTime(now.plusMinutes(5)); // 다음 시도까지 5분 동안 기다리게 해
                        smsVerificationRepository.save(smsVerification);
                    }
                }
            }
        }
        return false;
    }

    @Transactional
    public void updateSmsVerification(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId()); // 유저 정보 찾아

        if (optionalUser.isPresent()) { // 유저 정보가 있으면
            User savedUser = optionalUser.get();
            savedUser.setSmsVerified(true); // 인증 상태를 완료로 변경해
            userRepository.save(savedUser); // 유저 정보 저장해
        } else {
            throw new IllegalStateException("User not found in the UserRepository."); // 유저 정보가 없으면 에러 던져
        }
    }
}