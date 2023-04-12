// SmsService.java
package io.sinsabridge.backend.sms.service;

import io.sinsabridge.backend.sms.domain.entity.SmsVerification;
import io.sinsabridge.backend.sms.domain.repository.SmsHistoryRepository;

import io.sinsabridge.backend.sms.domain.entity.SmsHistory;
import io.sinsabridge.backend.sms.domain.repository.SmsVerificationRepository;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsSender smsSender;
    private final SmsHistoryRepository smsHistoryRepository;
    private final SmsVerificationRepository smsVerificationRepository;

    @Transactional
    public SmsSendResponse sendVerificationCode(String phoneNumber, String ipAddress) {
        String verificationCode = generateVerificationCode();

        SmsSendRequest request = SmsSendRequest.builder()
                .receiver(phoneNumber)
                .msg("인증번호: " + verificationCode)
                .build();

        SmsSendResponse response = smsSender.send(request);

        if (response.isSuccess()) {
            SmsHistory smsHistory = SmsHistory.builder()
                    .phoneNumber(phoneNumber)
                    .sentAt(LocalDateTime.now())
                    .ipAddress(ipAddress)
                    .build();

            smsHistoryRepository.save(smsHistory);
        }

        return response;
    }

    private String generateVerificationCode() {
        int randomCode = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(randomCode);
    }

    public Optional<SmsHistory> findRecentSmsHistory(String phoneNumber) {
        List<SmsHistory> smsHistoryList = smsHistoryRepository.findAllByPhoneNumber(phoneNumber);

        if (smsHistoryList.isEmpty()) {
            return Optional.empty();
        }

        SmsHistory mostRecentSmsHistory = smsHistoryList.stream()
                .max(Comparator.comparing(SmsHistory::getSentAt))
                .orElseThrow(() -> new IllegalStateException("Unexpected error while finding recent SMS history."));

        return Optional.of(mostRecentSmsHistory);
    }

    public boolean verifySmsCode(String phoneNumber, String code) {
        SmsVerification smsVerification = smsVerificationRepository.findByPhoneNumber(phoneNumber);

        if (smsVerification == null) {
            return false;
        }

        long minutesDifference = ChronoUnit.MINUTES.between(smsVerification.getVerificationTime(), LocalDateTime.now());

        return smsVerification.getVerificationCode().equals(code) && minutesDifference <= 5;
    }
}
