package io.sinsabridge.backend.infrastructure.mapper;

import io.sinsabridge.backend.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    void insert(User user);

    void update(User user);

    void deleteByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}

