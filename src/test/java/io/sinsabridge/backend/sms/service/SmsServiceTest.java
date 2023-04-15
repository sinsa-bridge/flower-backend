// SmsServiceTest.java

package io.sinsabridge.backend.sms.service;

import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.sms.domain.entity.SmsHistory;
import io.sinsabridge.backend.sms.domain.repository.SmsHistoryRepository;
import io.sinsabridge.backend.sms.domain.repository.SmsVerificationRepository;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<SmsHistory> smsHistoryCaptor;

    @Test
    void testSendVerificationCode() {
        // given
        String phoneNumber = "01012345678";
        String ipAddress = "192.168.0.1";
        String result_code = "1";
        String verificationCode = "1234";

        // SmsSender에서 받을 가짜 응답을 만들어 줘
        SmsSendResponse mockResponse = SmsSendResponse.builder()
                .result_code(result_code)
                .message("success")
                .msg_id("12345")
                .success_cnt("1")
                .error_cnt("0")
                .msg_type("SMS")
                .verification_code(verificationCode)
                .build();

        when(smsSender.send(any(SmsSendRequest.class))).thenReturn(mockResponse);

        // when
        // sendVerificationCode 메소드 호출하고
        SmsSendResponse response = smsService.sendVerificationCode(phoneNumber);

        // then
        // 결과가 성공적인지 확인해 봐
        assertTrue(response.isSuccess());

        // smsSender.send 메소드가 한 번 호출됐는지 확인해
        verify(smsSender, times(1)).send(any(SmsSendRequest.class));

        // smsHistoryRepository.save 메소드가 한 번 호출됐는지 확인하고,
        // 저장되는 SmsHistory 객체를 캡쳐해 봐
        verify(smsHistoryRepository, times(1)).save(smsHistoryCaptor.capture());

        // 캡쳐한 SmsHistory 객체에서 값들 확인해 봐
        SmsHistory savedSmsHistory = smsHistoryCaptor.getValue();
        assertThat(savedSmsHistory.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedSmsHistory.getVerificationCode()).isEqualTo(verificationCode);
    }
}
