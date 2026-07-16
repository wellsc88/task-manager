package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.dto.request.UpdateRoleRequest;
import com.well.tech.task.manager.dto.request.UpdateStatusRequest;
import com.well.tech.task.manager.dto.response.AdminUserResponse;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<AdminUserResponse> findAll() {

        return userRepository.findAll()
            .stream()
            .map(user -> new AdminUserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().getName(),
                    user.isEnabled()
            ))
            .toList();
    }

    @Transactional
    public void updateRole(
            UUID userId,
            UpdateRoleRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        user.setRole(role);

        userRepository.save(user);
    }

    @Transactional
    public void updateStatus(
            UUID userId,
            UpdateStatusRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        user.setEnabled(request.enabled());

        userRepository.save(user);
    }
}