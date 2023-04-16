package io.sinsabridge.backend.presentation.util;

import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import io.sinsabridge.backend.sms.service.SmsSender;

public class TestSmsSender implements SmsSender {

    @Override
    public SmsSendResponse send(SmsSendRequest request) {
        // 항상 동일한 인증 코드를 반환하는 응답 객체를 생성합니다.
        SmsSendResponse response = SmsSendResponse.builder()
                .result_code("1")
                .message("success")
                .msg_id("12345")
                .success_cnt("1")
                .error_cnt("0")
                .msg_type("SMS")
                .verification_code("1234")
                .build();

        return response;
    }
}
