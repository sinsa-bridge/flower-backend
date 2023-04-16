package io.sinsabridge.backend.sms.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class SmsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column
    private String verificationCode; // 인증에 사용된 4자리 코드

    @Column(nullable = false) // 변경: nullable 속성 추가
    private String resultCode;

    @Column(nullable = false) // 변경: nullable 속성 추가
    private String message;

    @Column(nullable = false) // 변경: nullable 속성 추가
    private String msgId;

    @Column(nullable = false) // 변경: nullable 속성 추가
    private int successCnt; // 변경: int 타입으로 수정

    @Column(nullable = false) // 변경: nullable 속성 추가
    private int errorCnt; // 변경: int 타입으로 수정

    @Column(nullable = false) // 변경: nullable 속성 추가
    private String msgType;


    @Builder
    public SmsHistory(String phoneNumber, LocalDateTime sentAt, String verificationCode, String resultCode, String message, String msgId, int successCnt, int errorCnt, String msgType) {
        this.phoneNumber = phoneNumber;
        this.sentAt = sentAt;
        this.verificationCode = verificationCode;
        this.resultCode = resultCode;
        this.message = message;
        this.msgId = msgId;
        this.successCnt = successCnt;
        this.errorCnt = errorCnt;
        this.msgType = msgType;
    }
}
