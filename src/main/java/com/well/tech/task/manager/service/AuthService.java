package com.well.tech.task.manager.service;

import com.well.tech.task.manager.dto.request.LoginRequest;
import com.well.tech.task.manager.dto.response.LoginResponse;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.UserRepository;
import com.well.tech.task.manager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );


        User user = repository.findByEmail(request.email())
                .orElseThrow();


        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail()
        );


        return new LoginResponse(token);
    }
}
