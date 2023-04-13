package io.sinsabridge.backend.sms.domain.entity;

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

    @Column(nullable = false)
    private String ipAddress;

    @Column
    private String verificationCode;

    @Builder
    public SmsHistory(String phoneNumber, LocalDateTime sentAt, String ipAddress, String verificationCode) {
        this.phoneNumber = phoneNumber;
        this.sentAt = sentAt;
        this.ipAddress = ipAddress;
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}