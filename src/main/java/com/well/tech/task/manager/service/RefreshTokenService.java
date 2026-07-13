package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.entity.RefreshToken;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;


    public RefreshToken createOrUpdate(User user) {

        return repository.findByUser(user)
                .map(this::updateToken)
                .orElseGet(() -> createToken(user));
    }


    private RefreshToken updateToken(RefreshToken refreshToken) {

        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        refreshToken.setExpiresAt(
                Instant.now()
                        .plusMillis(refreshExpiration)
        );

        return repository.save(refreshToken);
    }


    private RefreshToken createToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(
                        Instant.now()
                                .plusMillis(refreshExpiration)
                )
                .build();

        return repository.save(refreshToken);
    }


    public RefreshToken findByToken(String token) {

        return repository.findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refresh token not found"
                        )
                );
    }


    public RefreshToken verifyExpiration(
            RefreshToken refreshToken
    ) {

        if (refreshToken.getExpiresAt()
                .isBefore(Instant.now())) {

            repository.delete(refreshToken);

            throw new ResourceNotFoundException(
                    "Refresh token expired"
            );
        }

        return refreshToken;
    }


    public void deleteByUser(User user) {

        repository.deleteByUser(user);
    }
}