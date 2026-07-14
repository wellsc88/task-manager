package com.well.tech.task.manager.controller;

import com.well.tech.task.manager.dto.request.UpdateRoleRequest;
import com.well.tech.task.manager.dto.request.UpdateStatusRequest;
import com.well.tech.task.manager.dto.response.AdminUserResponse;
import com.well.tech.task.manager.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService service;

    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoleRequest request
    ) {

        service.updateRole(id, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {

        service.updateStatus(id, request);

        return ResponseEntity.noContent().build();
    }
}