package io.sinsabridge.backend.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinsabridge.backend.application.service.UserService;
import io.sinsabridge.backend.domain.entity.User;
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
        // 예제 사용자 생성
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber("01012345678");

        // UserService의 registerUser 메서드가 예제 사용자를 반환하도록 설정
        doAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            userArg.setId(1L);
            return null;
        }).when(userService).registerUser(any(User.class));

        // POST 요청을 보내고 응답을 검증
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("phoneNumber").value("01012345678"));

        // registerUser가 호출되었는지 검증
        verify(userService, times(1)).registerUser(any(User.class));
    }

    // 사용자 정보 업데이트 테스트
    @Test
    public void testUpdateUser() throws Exception {
        // 예제 사용자 생성
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber("01012345678");

        // UserService의 updateUser 메서드가 예제 사용자를 반환하도록 설정
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);

        // PUT 요청을 보내고 응답을 검증
        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("phoneNumber").value("01012345678"));

        // updateUser가 호출되었는지 검증
        verify(userService, times(1)).updateUser(anyLong(), any(User.class));
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
        User user1 = createUser(1L, "010-1234-5678", "password1", "MALE");
        User user2 = createUser(2L, "010-2345-6789", "password2", "FEMALE");

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
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)));

        // findAllUsers가 호출되었는지 검증
        verify(userService, times(1)).findAllUsers(any(Pageable.class));
    }

    @Test
    public void testCreateUserCase2() throws Exception {
        // 예제 사용자 생성
        User user = createUser(null, "010-1234-5678", "password", "MALE");

        // userService의 registerUser 메서드가 호출되었을 때 아무 작업도 수행하지 않도록 설정
        doNothing().when(userService).registerUser(any(User.class));

        // POST 요청을 보내고 응답을 검증
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.gender", is(user.getGender())));

        // registerUser가 호출되었는지 검증
        verify(userService, times(1)).registerUser(any(User.class));
    }

    private User createUser(Long id, String phoneNumber, String password, String gender) {
        return User.builder()
                .id(id)
                .phoneNumber(phoneNumber)
                .password(password)
                .gender(gender)
                .build();
    }

}