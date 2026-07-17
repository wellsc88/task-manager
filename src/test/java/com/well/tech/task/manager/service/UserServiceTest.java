package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.auth.EmailAlreadyExistsException;
import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.dto.request.UserRequest;
import com.well.tech.task.manager.dto.response.UserResponse;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.mapper.UserMapper;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    @Test
    void shouldCreateUserSuccessfully() {

        UserRequest request = new UserRequest(
                "John Doe",
                "john@email.com",
                "12345678"
        );

        Role role = new Role();
        role.setName("USER");

        User user = new User();

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());

        UserResponse response = new UserResponse(
                savedUser.getId(),
                "John Doe",
                "john@email.com"
        );

        when(repository.existsByEmail(request.email()))
                .thenReturn(false);

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(role));

        when(mapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");

        when(repository.save(user))
                .thenReturn(savedUser);

        when(mapper.toResponse(savedUser))
                .thenReturn(response);

        UserResponse result =
                service.create(request);

        assertThat(result)
                .isNotNull();

        assertThat(result.email())
                .isEqualTo("john@email.com");

        verify(repository)
                .existsByEmail(request.email());

        verify(roleRepository)
                .findByName("USER");

        verify(passwordEncoder)
                .encode(request.password());

        verify(repository)
                .save(user);

        verify(mapper)
                .toResponse(savedUser);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        UserRequest request = new UserRequest(
                "John Doe",
                "john@email.com",
                "12345678"
        );

        when(repository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.create(request)
        )
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email already registered");

        verify(repository)
                .existsByEmail(request.email());

        verifyNoInteractions(
                roleRepository,
                mapper,
                passwordEncoder
        );
    }

    @Test
    void shouldThrowExceptionWhenUserRoleNotFound() {

        UserRequest request = new UserRequest(
                "John Doe",
                "john@email.com",
                "12345678"
        );

        when(repository.existsByEmail(request.email()))
                .thenReturn(false);

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.create(request)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Role USER not found");

        verify(roleRepository)
                .findByName("USER");

        verifyNoInteractions(
                mapper,
                passwordEncoder
        );
    }
}