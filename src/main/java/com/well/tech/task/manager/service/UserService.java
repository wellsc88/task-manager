package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.auth.EmailAlreadyExistsException;
import com.well.tech.task.manager.dto.request.UserRequest;
import com.well.tech.task.manager.dto.response.UserResponse;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.mapper.UserMapper;
import com.well.tech.task.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(UserRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = mapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        return mapper.toResponse(repository.save(user));
    }
}