// SmsSendResponse.java
package io.sinsabridge.backend.sms.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendResponse {
    private static final String SUCCESS_STATUS = "2";

    private String messageId;
    private String messageStatus;
    private String errorCode;
    private String errorMessage;

    public boolean isSuccess() {
        return SUCCESS_STATUS.equals(messageStatus);
    }
}
