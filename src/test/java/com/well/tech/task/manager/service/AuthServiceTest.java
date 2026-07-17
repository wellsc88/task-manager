package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.auth.InvalidCredentialsException;
import com.well.tech.task.manager.common.exceptions.auth.UserDisabledException;
import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.dto.request.LoginRequest;
import com.well.tech.task.manager.dto.request.RefreshTokenRequest;
import com.well.tech.task.manager.dto.response.LoginResponse;
import com.well.tech.task.manager.dto.response.RefreshTokenResponse;
import com.well.tech.task.manager.entity.RefreshToken;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.UserRepository;
import com.well.tech.task.manager.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository repository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService service;

    @Test
    void shouldLoginSuccessfully() {

        LoginRequest request =
                new LoginRequest(
                        "john@email.com",
                        "123456"
                );

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.email());

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("refresh-token")
                        .user(user)
                        .build();

        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(
                any(),
                any(),
                any()
        ))
                .thenReturn("access-token");

        when(refreshTokenService.createOrUpdate(user))
                .thenReturn(refreshToken);

        LoginResponse response =
                service.login(request);

        assertThat(response.accessToken())
                .isEqualTo("access-token");

        assertThat(response.refreshToken())
                .isEqualTo("refresh-token");

        verify(authenticationManager)
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class
                ));

        verify(jwtService)
                .generateToken(
                        user.getId(),
                        user.getEmail(),
                        user.getRole()
                );
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {

        LoginRequest request =
                new LoginRequest(
                        "john@email.com",
                        "wrong"
                );

        doThrow(new BadCredentialsException(""))
                .when(authenticationManager)
                .authenticate(any());

        assertThatThrownBy(() ->
                service.login(request)
        )
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid email or password");

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowExceptionWhenUserIsDisabled() {

        LoginRequest request =
                new LoginRequest(
                        "john@email.com",
                        "123456"
                );

        doThrow(new DisabledException(""))
                .when(authenticationManager)
                .authenticate(any());

        assertThatThrownBy(() ->
                service.login(request)
        )
                .isInstanceOf(UserDisabledException.class)
                .hasMessage("User account is disabled");

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        LoginRequest request =
                new LoginRequest(
                        "john@email.com",
                        "123456"
                );

        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.login(request)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldGenerateNewAccessTokenUsingRefreshToken() {

        RefreshTokenRequest request =
                new RefreshTokenRequest(
                        "refresh-token"
                );

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("john@email.com");

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("refresh-token")
                        .user(user)
                        .build();

        when(refreshTokenService.findByToken(
                request.refreshToken()
        ))
                .thenReturn(refreshToken);

        when(jwtService.generateToken(
                any(),
                any(),
                any()
        ))
                .thenReturn("new-access-token");

        RefreshTokenResponse response =
                service.refreshToken(request);

        assertThat(response.accessToken())
                .isEqualTo("new-access-token");

        verify(refreshTokenService)
                .verifyExpiration(refreshToken);
    }

    @Test
    void shouldLogoutSuccessfully() {

        service.logout("refresh-token");

        verify(refreshTokenService)
                .deleteByToken("refresh-token");
    }
}