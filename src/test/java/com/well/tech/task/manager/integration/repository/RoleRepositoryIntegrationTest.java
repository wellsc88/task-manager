package com.well.tech.task.manager.integration.repository;

import com.well.tech.task.manager.integration.config.AbstractPostgresContainer;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class RoleRepositoryIntegrationTest extends AbstractPostgresContainer {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveRole() {

        Role role = createRole();

        Role saved =
                roleRepository.save(role);

        assertThat(saved.getId())
                .isNotNull();

        assertThat(saved.getName())
                .isEqualTo("USER");

        assertThat(saved.getDescription())
                .isEqualTo("Default user role");
    }

    @Test
    void shouldFindRoleByName() {

        Role role =
                roleRepository.findByName("USER")
                        .orElseThrow();

        Optional<Role> result =
                roleRepository.findByName("USER");

        assertThat(result)
                .isPresent();

        assertThat(result.get().getId())
                .isEqualTo(role.getId());
    }

    @Test
    void shouldReturnEmptyWhenRoleDoesNotExist() {

        Optional<Role> result =
                roleRepository.findByName("NOT_FOUND");

        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldNotAllowDuplicateRoleName() {

        roleRepository.save(
                createRole()
        );

        assertThatThrownBy(() ->
                roleRepository.saveAndFlush(
                        createRole()
                )
        )
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Role createRole() {

        return Role.builder()
                .name("USER")
                .description("Default user role")
                .build();
    }
}