// SmsServiceTest.java

package io.sinsabridge.backend.sms.service;

import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import io.sinsabridge.backend.sms.domain.entity.SmsHistory;
import io.sinsabridge.backend.sms.domain.repository.SmsHistoryRepository;
import io.sinsabridge.backend.sms.domain.repository.SmsVerificationRepository;
import io.sinsabridge.backend.domain.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    @Mock
    private SmsSender smsSender;

    @Mock
    private SmsHistoryRepository smsHistoryRepository;

    @Mock
    private SmsVerificationRepository smsVerificationRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testSendVerificationCode() {
        // given
        String phoneNumber = "01012345678";
        String ipAddress = "192.168.0.1";
        String result_code = "1";

        SmsSendResponse mockResponse = SmsSendResponse.builder()
                .result_code(result_code)
                .message("success")
                .msg_id("12345")
                .success_cnt("1")
                .error_cnt("0")
                .msg_type("SMS")
                .build();

        when(smsSender.send(any(SmsSendRequest.class))).thenReturn(mockResponse);

        // when
        SmsSendResponse response = smsService.sendVerificationCode(phoneNumber, ipAddress);

        // then
        assertTrue(response.isSuccess());
        verify(smsSender, times(1)).send(any(SmsSendRequest.class));
        verify(smsHistoryRepository, times(1)).save(any(SmsHistory.class));
    }
}
