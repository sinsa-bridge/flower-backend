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
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    @Column(name = "verification_attempts", nullable = false)
    private int verificationAttempts;

    @Column(name = "last_attempt_time")
    private LocalDateTime lastAttemptTime;

    @Column(name = "verification_time")
    private LocalDateTime verificationTime;

}
