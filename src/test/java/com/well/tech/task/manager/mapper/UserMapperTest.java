package com.well.tech.task.manager.mapper;

import com.well.tech.task.manager.dto.request.UserRequest;
import com.well.tech.task.manager.dto.response.UserResponse;
import com.well.tech.task.manager.entity.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void shouldConvertRequestToEntity() {

        UserRequest request =
                new UserRequest(
                        "John Doe",
                        "john@test.com",
                        "password123"
                );

        User user = mapper.toEntity(request);

        assertThat(user)
                .isNotNull();

        assertThat(user.getName())
                .isEqualTo("John Doe");

        assertThat(user.getEmail())
                .isEqualTo("john@test.com");

        assertThat(user.getPassword())
                .isEqualTo("password123");
    }

    @Test
    void shouldConvertEntityToResponse() {

        UUID id = UUID.randomUUID();

        User user =
                User.builder()
                        .id(id)
                        .name("John Doe")
                        .email("john@test.com")
                        .password("encoded-password")
                        .build();

        UserResponse response =
                mapper.toResponse(user);

        assertThat(response)
                .isNotNull();

        assertThat(response.id())
                .isEqualTo(id);

        assertThat(response.name())
                .isEqualTo("John Doe");

        assertThat(response.email())
                .isEqualTo("john@test.com");
    }

    @Test
    void shouldCreateResponseWithoutPassword() {

        User user =
                User.builder()
                        .id(UUID.randomUUID())
                        .name("Mary")
                        .email("mary@test.com")
                        .password("secret")
                        .build();

        UserResponse response =
                mapper.toResponse(user);

        assertThat(response)
                .doesNotHaveToString("secret");
    }
}