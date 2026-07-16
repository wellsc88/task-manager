package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.entity.RefreshToken;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository repository;

    @InjectMocks
    private RefreshTokenService service;

    private User user;

    @BeforeEach
    void setup() throws Exception {

        user = new User();

        Field field =
                RefreshTokenService.class
                        .getDeclaredField("refreshExpiration");

        field.setAccessible(true);

        field.set(service, 3600000L);
    }

    @Test
    void shouldCreateNewRefreshTokenWhenNotExists() {

        when(repository.findByUser(user))
                .thenReturn(Optional.empty());

        when(repository.save(any(RefreshToken.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        RefreshToken result =
                service.createOrUpdate(user);

        assertThat(result.getUser())
                .isEqualTo(user);

        assertThat(result.getToken())
                .isNotBlank();

        assertThat(result.getExpiresAt())
                .isAfter(Instant.now());

        verify(repository)
                .save(any(RefreshToken.class));
    }

    @Test
    void shouldUpdateExistingRefreshToken() {

        RefreshToken token = new RefreshToken();

        token.setToken("old-token");

        when(repository.findByUser(user))
                .thenReturn(Optional.of(token));

        when(repository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        RefreshToken result =
                service.createOrUpdate(user);

        assertThat(result.getToken())
                .isNotEqualTo("old-token");

        assertThat(result.getExpiresAt())
                .isAfter(Instant.now());

        verify(repository)
                .save(token);
    }

    @Test
    void shouldFindToken() {

        RefreshToken token =
                new RefreshToken();

        when(repository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        RefreshToken result =
                service.findByToken("abc");

        assertThat(result)
                .isEqualTo(token);

        verify(repository)
                .findByToken("abc");
    }

    @Test
    void shouldThrowWhenTokenNotFound() {

        when(repository.findByToken("abc"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.findByToken("abc")
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Refresh token not found");
    }

    @Test
    void shouldThrowWhenTokenExpired() {

        RefreshToken token =
                new RefreshToken();

        token.setExpiresAt(
                Instant.now().minusSeconds(10)
        );

        assertThatThrownBy(() ->
                service.verifyExpiration(token)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Refresh token expired");

        verify(repository)
                .delete(token);
    }

    @Test
    void shouldNotDeleteValidToken() {

        RefreshToken token =
                new RefreshToken();

        token.setExpiresAt(
                Instant.now().plusSeconds(1000)
        );

        service.verifyExpiration(token);

        verify(repository, never())
                .delete(any());
    }

    @Test
    void shouldDeleteToken() {

        RefreshToken token =
                new RefreshToken();

        when(repository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        service.deleteByToken("abc");

        verify(repository)
                .delete(token);
    }
}