package io.sinsabridge.backend.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private String errorCode;  // 에러 코드
    private String errorMessage;  // 에러 메시지
    private List<String> errors;  // 에러 메시지 목록

    // Getter, Setter
}
