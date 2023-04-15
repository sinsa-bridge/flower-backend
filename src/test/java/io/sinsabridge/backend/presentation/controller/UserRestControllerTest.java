package io.sinsabridge.backend.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinsabridge.backend.application.service.UserService;
import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.presentation.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserRestController.class)
@RequiredArgsConstructor

public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    // 사용자 생성 테스트
    @Test
    public void testCreateUserCase1() throws Exception {
        // given: 테스트에 필요한 변수를 설정합니다.
        UserDto user = new UserDto();
        user.setPhoneNumber("01012345678");
        user.setPassword("wonhwa");
        user.setAge("14");
        user.setGender(User.Gender.FEMALE);
        user.setNickName("wonhwa");
        user.setRegion("seoul");
        // UserService가 예제 사용자를 반환하도록 설정합니다.
        doAnswer(invocation -> {
            UserDto userArg = invocation.getArgument(0);
            userArg.setAge("23");
            userArg.setRegion("seoul");
            userArg.setPhoneNumber("01012345678");
            userArg.setNickName("wonhwa");
            userArg.setGender(User.Gender.FEMALE);
            userArg.setPassword("0109291");
            return userArg;
        }).when(userService).registerUser(any(UserDto.class));

        // when: POST 요청을 보내고 응답을 검증합니다.
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))

                // then: 응답 상태 코드와 body를 검증합니다.
                .andExpect(status().isCreated())
               // .andExpect(jsonPath("id").isEmpty())
                .andExpect(jsonPath("phoneNumber").value("01012345678"));

        // verify: UserService의 registerUser 메서드가 한 번 호출되었는지 검증합니다.
        verify(userService, times(1)).registerUser(any(UserDto.class));
    }


    // 사용자 정보 업데이트 테스트
    @Test
    public void testUpdateUser() throws Exception {

        String requestJson = "{\"id\":1,\"phoneNumber\":\"01012345678\",\"nickName\":null,\"password\":\"testpassword\",\"gender\":null,\"age\":null,\"hobbies\":null,\"region\":null,\"profileImage\":null,\"paid\":false,\"active\":false,\"smsVerified\":false,\"smsVerificationTimestamp\":null}";

        // 예제 사용자 생성
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber("01012345678");

        // UserService의 updateUser 메서드가 예제 사용자를 반환하도록 설정
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(user);

        // PUT 요청을 보내고 응답을 검증
        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("phoneNumber").value("01012345678"));

        // updateUser가 호출되었는지 검증
        verify(userService, times(1)).updateUser(anyLong(), any(UserDto.class));
    }

    // 사용자 삭제 테스트
    @Test
    public void testDeleteUser() throws Exception {
        // UserService의 deleteUser 메서드가 호출되었을 때 아무 동작도 하지 않도록 설정
        doNothing().when(userService).deleteUser(anyLong());

        // DELETE 요청을 보내고 응답을 검증
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        // deleteUser가 호출되었는지 검증
        verify(userService, times(1)).deleteUser(anyLong());
    }

    // 페이징 처리된 전체 사용자 목록 테스트
    @Test
    public void testGetAllUsers() throws Exception {
        // 예제 사용자 생성
        User user1 = createUser(1L, "01012345678", "password1", User.Gender.MALE);
        User user2 = createUser(2L, "01023456789", "password2", User.Gender.FEMALE);

        // 페이징 처리된 사용자 목록 생성
        List<User> users = Arrays.asList(user1, user2);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 2), 2);

        // userService의 findAllUsers 메서드가 호출되었을 때 페이징 처리된 사용자 목록을 반환하도록 설정
        when(userService.findAllUsers(any(Pageable.class))).thenReturn(userPage);

        // GET 요청을 보내고 응답을 검증
        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userList", hasSize(2))) // JSON 경로 변경
                .andExpect(jsonPath("$._embedded.userList[0].id", is(1))) // JSON 경로 변경
                .andExpect(jsonPath("$._embedded.userList[1].id", is(2))); // JSON 경로 변경

        // findAllUsers가 호출되었는지 검증
        verify(userService, times(1)).findAllUsers(any(Pageable.class));
    }


    @Test
    public void testCreateUserCase2() throws Exception {
        // 예제 사용자 생성
        User user = createUser(null, "010-1234-5678", "password", User.Gender.MALE);

        // 예제 사용자 DTO 생성
        UserDto userDto = new UserDto();
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setGender(user.getGender());
        userDto.setPassword(user.getPassword());

        // userService의 registerUser 메서드가 호출되었을 때 userDto를 반환하도록 설정
        when(userService.registerUser(any(UserDto.class))).thenReturn(userDto);

        // POST 요청을 보내고 응답을 검증
        mockMvc.perform(post("/api/users/register") // 요청 메서드를 GET에서 POST로 변경
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))) // userDto로 변경
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.gender", is(user.getGender().toString())));

        // registerUser가 호출되었는지 검증
        verify(userService, times(1)).registerUser(any(UserDto.class));
    }


    private User createUser(Long id, String phoneNumber, String password, User.Gender gender) {
        return User.builder()
                .id(id)
                .phoneNumber(phoneNumber)
                .password(password)
                .gender(gender)
                .build();
    }

}