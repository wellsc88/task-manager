package com.well.tech.task.manager.integration.repository;

import com.well.tech.task.manager.integration.config.AbstractPostgresContainer;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class UserRepositoryIntegrationTest extends AbstractPostgresContainer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveUser() {

        Role role = createRole();

        User user = createUser(role);

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName())
                .isEqualTo("John Doe");

        assertThat(saved.getEmail())
                .isEqualTo("john@test.com");

        assertThat(saved.isEnabled())
                .isTrue();
    }

    @Test
    void shouldFindUserByEmail() {

        Role role = createRole();

        User user = createUser(role);

        userRepository.save(user);

        Optional<User> result =
                userRepository.findByEmail("john@test.com");

        assertThat(result)
                .isPresent();

        assertThat(result.get().getName())
                .isEqualTo("John Doe");
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {

        Optional<User> result =
                userRepository.findByEmail("notfound@test.com");

        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {

        Role role = createRole();

        User user = createUser(role);

        userRepository.save(user);

        boolean exists =
                userRepository.existsByEmail("john@test.com");

        assertThat(exists)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        boolean exists =
                userRepository.existsByEmail("unknown@test.com");

        assertThat(exists)
                .isFalse();
    }

    @Test
    void shouldNotAllowDuplicateEmail() {

        Role role = createRole();

        User firstUser =
                createUser(role);

        User secondUser =
                User.builder()
                        .name("Jane Doe")
                        .email("john@test.com")
                        .password("654321")
                        .role(role)
                        .build();

        userRepository.save(firstUser);

        assertThatThrownBy(() ->
                userRepository.saveAndFlush(secondUser)
        )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Role createRole() {

        return roleRepository.findByName("USER")
                .orElseGet(() ->
                        roleRepository.save(
                                Role.builder()
                                        .name("USER")
                                        .description("Default user role")
                                        .build()
                        )
                );
    }

    private User createUser(Role role) {

        return User.builder()
                .name("John Doe")
                .email("john@test.com")
                .password("123456")
                .role(role)
                .build();
    }
}