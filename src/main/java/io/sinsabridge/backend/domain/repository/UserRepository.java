package io.sinsabridge.backend.domain.repository;

import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.presentation.exception.UserNotFoundException;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByPhoneNumber(String phoneNumber) throws UserNotFoundException;

    void save(User user);

    void update(User user);

    void delete(String phoneNumber);
}
