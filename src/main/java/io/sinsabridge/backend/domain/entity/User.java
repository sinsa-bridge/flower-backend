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
@Table(name = "Users")
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

    @Column(nullable = true)
    private String birthDate;

    @Column
    private String hobbies;

    @Column
    private String region;

    @Column
    private String profileImage;

    @Column
    private boolean paid ;

    @Column
    private boolean active;

    @Column
    private boolean smsVerified;

    @Column
    private LocalDateTime smsVerificationTimestamp;

    public void setSmsVerified(boolean smsVerified) {
        this.smsVerified = smsVerified;
    }
}
