// User.java
package io.sinsabridge.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private LocalDateTime birthDate;

    @Column(nullable = false)
    private String hobbies;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private boolean paid;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime smsVerificationTimestamp;
}
