package io.sinsabridge.backend.domain.repository.impl;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.domain.repository.UserRepository;
import io.sinsabridge.backend.infrastructure.mapper.UserMapper;
import io.sinsabridge.backend.presentation.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;

    @Autowired
    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

/*    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userMapper.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
    }*/

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) throws UserNotFoundException {
        Optional<User> user = userMapper.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found with phone number: " + phoneNumber);
        }
        return user;
    }

    @Override
    public void save(User user) {
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void delete(String phoneNumber) {
        userMapper.deleteByPhoneNumber(phoneNumber);
    }
}
