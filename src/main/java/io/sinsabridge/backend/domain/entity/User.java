package io.sinsabridge.backend.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private String nickName;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = new HashSet<>();

    private Gender gender;

    public enum Role {
        USER, ADMIN
    }

    // 사용자에게 역할을 추가하는 메소드
    public void addRole(Role role) {
        roles.add(role);
    }

    // 사용자 역할을 제거하는 메소드
    public void removeRole(Role role) {
        roles.remove(role);
    }

    // 사용자가 특정 역할을 가지고 있는지 확인하는 메소드
    public boolean hasRole(Role role) {
        return roles.contains(role);
    }
    @Column(nullable = false)
    private String age;

    private String hobbies;

    @Column(nullable = false)
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
