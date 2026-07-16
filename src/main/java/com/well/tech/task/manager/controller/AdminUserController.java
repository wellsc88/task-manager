package com.well.tech.task.manager.controller;

import com.well.tech.task.manager.dto.request.UpdateRoleRequest;
import com.well.tech.task.manager.dto.request.UpdateStatusRequest;
import com.well.tech.task.manager.dto.response.AdminUserResponse;
import com.well.tech.task.manager.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(
        name = "Admin Users",
        description = "Administrative operations for user management"
)
public class AdminUserController {

    private final AdminUserService service;

    @Operation(
            summary = "List all users",
            description = "Returns all registered users for administrative management"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(
                            schema = @Schema(
                                    implementation = AdminUserResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }


    @Operation(
            summary = "Update user role",
            description = "Updates the role assigned to a user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User role updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid role data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoleRequest request
    ) {

        service.updateRole(id, request);

        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Update user status",
            description = "Updates the active status of a user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User status updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {

        service.updateStatus(id, request);

        return ResponseEntity.noContent().build();
    }
}