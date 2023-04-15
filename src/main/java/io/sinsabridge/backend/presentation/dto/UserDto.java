package io.sinsabridge.backend.presentation.dto;

import io.sinsabridge.backend.domain.entity.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {


    // 회원 가입 요청을 처리할 DTO
    @NotEmpty(message = "전화번호는 필수로 입력해야 해요!")
    private String phoneNumber;

    @NotEmpty(message = "비밀번호는 필수로 입력해야 해요!")
    @Size(min = 6, message = "비밀번호는 최소 6자리여야 해요!")
    private String password;

    private String nickName;

    private User.Gender gender;

    private String age;


    private String region;

}