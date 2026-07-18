package com.well.tech.task.manager.integration.repository;

import com.well.tech.task.manager.integration.config.AbstractPostgresContainer;
import com.well.tech.task.manager.entity.RefreshToken;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RefreshTokenRepository;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class RefreshTokenRepositoryIntegrationTest extends AbstractPostgresContainer {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveRefreshToken() {

        User user = createUser();

        RefreshToken refreshToken =
                createRefreshToken(user);

        RefreshToken saved =
                refreshTokenRepository.save(refreshToken);

        assertThat(saved.getId())
                .isNotNull();

        assertThat(saved.getToken())
                .isEqualTo(refreshToken.getToken());

        assertThat(saved.getExpiresAt())
                .isNotNull();
    }

    @Test
    void shouldFindRefreshTokenByToken() {

        User user = createUser();

        RefreshToken refreshToken =
                createRefreshToken(user);

        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> result =
                refreshTokenRepository
                        .findByToken(refreshToken.getToken());

        assertThat(result)
                .isPresent();

        assertThat(result.get().getUser().getEmail())
                .isEqualTo(user.getEmail());
    }

    @Test
    void shouldFindRefreshTokenByUser() {

        User user = createUser();

        RefreshToken refreshToken =
                createRefreshToken(user);

        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> result =
                refreshTokenRepository.findByUser(user);

        assertThat(result)
                .isPresent();

        assertThat(result.get().getToken())
                .isEqualTo(refreshToken.getToken());
    }

    @Test
    void shouldDeleteRefreshTokenByUser() {

        User user = createUser();

        RefreshToken refreshToken =
                createRefreshToken(user);

        refreshTokenRepository.save(refreshToken);

        refreshTokenRepository.deleteByUser(user);

        Optional<RefreshToken> result =
                refreshTokenRepository.findByUser(user);

        assertThat(result)
                .isEmpty();
    }

    private RefreshToken createRefreshToken(User user) {

        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(
                        Instant.now()
                                .plus(7, ChronoUnit.DAYS)
                )
                .user(user)
                .build();
    }

    private User createUser() {

        Role role =
                roleRepository.findByName("USER")
                        .orElseGet(() ->
                                roleRepository.save(
                                        Role.builder()
                                                .name("USER")
                                                .description("Default role")
                                                .build()
                                )
                        );

        return userRepository.save(
                User.builder()
                        .name("John Doe")
                        .email(
                                "john_" +
                                        UUID.randomUUID() +
                                        "@test.com"
                        )
                        .password("123456")
                        .role(role)
                        .build()
        );
    }
}