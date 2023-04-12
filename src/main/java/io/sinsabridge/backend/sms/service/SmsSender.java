package io.sinsabridge.backend.sms.service;

import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;

public interface SmsSender {

    /**
     * 주어진 SmsSendRequest를 사용하여 SMS 메시지를 발송하고, 결과를 SmsSendResponse 형태로 반환합니다.
     *
     * @param request SMS 발송 요청 정보를 담은 SmsSendRequest 객체
     * @return 발송 결과를 담은 SmsSendResponse 객체
     */
    SmsSendResponse send(SmsSendRequest request);
}
