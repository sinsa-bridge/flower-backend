package io.sinsabridge.backend.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private String hobbies;

    private String region;

    private String profileImage;

    private boolean paid;

    private boolean active;

    private boolean smsVerified;

    @Column(nullable = false)
    private LocalDateTime smsVerificationTimestamp;

    public enum Gender {
        MALE, FEMALE
    }
}
