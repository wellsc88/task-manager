package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.auth.InvalidCredentialsException;
import com.well.tech.task.manager.common.exceptions.auth.UserDisabledException;
import com.well.tech.task.manager.dto.request.LoginRequest;
import com.well.tech.task.manager.dto.request.RefreshTokenRequest;
import com.well.tech.task.manager.dto.response.LoginResponse;
import com.well.tech.task.manager.dto.response.RefreshTokenResponse;
import com.well.tech.task.manager.entity.RefreshToken;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.UserRepository;
import com.well.tech.task.manager.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

        } catch (BadCredentialsException ex) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );

        } catch (DisabledException ex) {

            throw new UserDisabledException(
                    "User account is disabled"
            );
        }

        User user = repository.findByEmail(request.email())
                .orElseThrow();

        String accessToken = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        RefreshToken refreshToken =
                refreshTokenService.createOrUpdate(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    public RefreshTokenResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        request.refreshToken()
                );

        refreshTokenService.verifyExpiration(
                refreshToken
        );


        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );


        return new RefreshTokenResponse(
                accessToken
        );
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
    }
}