// SmsSendRequest.java
package io.sinsabridge.backend.sms.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsSendRequest {
    private String receiver;
    private String msg;

    public SmsSendRequest(String receiver) {
        this.receiver = receiver;
    }
}
