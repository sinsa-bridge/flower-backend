package io.sinsabridge.backend.application.service;

import io.sinsabridge.backend.application.dto.UserSignUpRequest;
import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.presentation.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(UserSignUpRequest signUpRequest) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        // User 객체 생성
        User user = new User();
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setGender(signUpRequest.getGender());
        user.setAge(signUpRequest.getAge());
        user.setHobby(signUpRequest.getHobby());
        user.setLocation(signUpRequest.getLocation());
        user.setPassword(encodedPassword);

        // 회원 정보 저장
        userRepository.save(user);

        return user;
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) throws UserNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}