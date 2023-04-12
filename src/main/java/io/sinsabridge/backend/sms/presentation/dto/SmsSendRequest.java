// SmsSendRequest.java
package io.sinsabridge.backend.sms.presentation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsSendRequest {
    private String receiver;
    private String msg;
}
