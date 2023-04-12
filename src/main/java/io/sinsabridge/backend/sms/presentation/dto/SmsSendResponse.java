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
    private static final String SUCCESS_STATUS = "1";

    private String result_code;
    private String message;
    private String msg_id;
    private String success_cnt;
    private String error_cnt;
    private String msg_type;




    public boolean isSuccess() {
        return SUCCESS_STATUS.equals(result_code);
    }
}
