package io.sinsabridge.backend.presentation.util;

import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AligoSmsServiceImplTest {

    @Autowired
    AligoSmsServiceImpl aligoSmsServiceImpl;

    @Test
    void aligo() {
        SmsSendRequest smsSendRequest = SmsSendRequest.builder()
                        .msg("프로그램 테스트").receiver("01087162830").build();
        aligoSmsServiceImpl.send(smsSendRequest);
    }
}