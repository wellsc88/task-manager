package com.well.tech.task.manager.mapper;

import com.well.tech.task.manager.common.enums.Role;
import com.well.tech.task.manager.dto.request.UserRequest;
import com.well.tech.task.manager.dto.response.UserResponse;
import com.well.tech.task.manager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {

        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(Role.USER)
                .build();
    }

    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}