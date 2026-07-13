package com.well.tech.task.manager.controller;

import com.well.tech.task.manager.dto.request.CreateUserRequest;
import com.well.tech.task.manager.dto.response.UserResponse;
import com.well.tech.task.manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(
            @Valid @RequestBody CreateUserRequest request) {

        return service.create(request);
    }
}