package io.sinsabridge.backend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String phoneNumber;

    private String password;

    private String gender;

    private String birthDate;

    private String hobbies;

    private String region;

    private String profileImage;

    private boolean paid ;

    private boolean active;

    private boolean smsVerified;

    private LocalDateTime smsVerificationTimestamp;



}