package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.dto.request.UpdateRoleRequest;
import com.well.tech.task.manager.dto.request.UpdateStatusRequest;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;


    @InjectMocks
    private AdminUserService service;


    @Test
    void shouldFindAllUsers() {

        Role role = new Role();
        role.setName("USER");


        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John");
        user.setEmail("john@test.com");
        user.setRole(role);
        user.setEnabled(true);

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        var result = service.findAll();

        assertThat(result)
                .hasSize(1);

        assertThat(result.getFirst().email())
                .isEqualTo("john@test.com");

        verify(userRepository)
                .findAll();
    }

    @Test
    void shouldUpdateUserRole() {

        UUID id = UUID.randomUUID();

        User user = new User();
        Role role = new Role();

        role.setName("ADMIN");

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        when(roleRepository.findByName("ADMIN"))
                .thenReturn(Optional.of(role));

        service.updateRole(
                id,
                new UpdateRoleRequest("ADMIN")
        );

        assertThat(user.getRole())
                .isEqualTo(role);

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldThrowWhenUserNotFoundUpdatingRole(){

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateRole(
                        id,
                        new UpdateRoleRequest("ADMIN")
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);

        verify(roleRepository, never())
                .findByName(any());
    }

    @Test
    void shouldThrowWhenRoleNotFound(){

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(new User()));

        when(roleRepository.findByName("ADMIN"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateRole(
                        id,
                        new UpdateRoleRequest("ADMIN")
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldUpdateUserStatus(){

        UUID id = UUID.randomUUID();

        User user = new User();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        service.updateStatus(
                id,
                new UpdateStatusRequest(false)
        );

        assertThat(user.isEnabled())
                .isFalse();

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldThrowWhenUserNotFoundUpdatingStatus(){

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateStatus(
                        id,
                        new UpdateStatusRequest(false)
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, never())
                .save(any());
    }

}