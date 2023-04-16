package io.sinsabridge.backend.sms.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SmsVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이 아이디는 인증 정보를 구분하기 위한거야

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber; // 이건 전화번호야

    @Column(name = "verification_code", nullable = false)
    private String verificationCode; // 여기는 인증 코드가 저장되어야 해

    @Column(name = "verified", nullable = false)
    private boolean verified; // 이건 인증이 완료되었는지 확인하는거야

    @Column(name = "verification_attempts", nullable = false)
    private int verificationAttempts; // 이건 인증 시도 횟수야

    @Column(name = "last_attempt_time")
    private LocalDateTime lastAttemptTime; // 여기는 마지막 인증 시도 시간이야

    @Column(name = "verification_time")
    private LocalDateTime verificationTime; // 인증 완료된 시간이 여기 저장되어야 해


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
