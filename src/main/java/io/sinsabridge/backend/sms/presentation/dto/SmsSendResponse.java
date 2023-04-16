// SmsSendResponse.java
package io.sinsabridge.backend.sms.presentation.dto;

import io.sinsabridge.backend.sms.domain.entity.SmsHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendResponse {
    private static final String SUCCESS_STATUS = "1";

    private String result_code;
    private String message;
    private String msg_id;
    private String success_cnt;
    private String error_cnt;
    private String msg_type;
    private String verification_code; // 추가된 프로퍼티

    public SmsHistory toSmsHistory(String phoneNumber, SmsSendResponse smsSendResponse) {
        return SmsHistory.builder()
                .phoneNumber(phoneNumber)
                .sentAt(LocalDateTime.now())
                .verificationCode(smsSendResponse.getVerification_code())
                .resultCode(smsSendResponse.getResult_code())
                .message(smsSendResponse.getMessage())
                .msgId(smsSendResponse.getMsg_id())
                .successCnt(Integer.parseInt(smsSendResponse.getSuccess_cnt()))
                .errorCnt(Integer.parseInt(smsSendResponse.getError_cnt()))
                .msgType(smsSendResponse.getMsg_type())
                .build();
    }


    public boolean isSuccess() {
        return SUCCESS_STATUS.equals(result_code);
    }
}
