package com.well.tech.task.manager.integration.repository;

import com.well.tech.task.manager.integration.config.AbstractPostgresContainer;
import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.Task;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.TaskRepository;
import com.well.tech.task.manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class TaskRepositoryIntegrationTest extends AbstractPostgresContainer {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveTask() {

        User user = createUser();

        Task task = createTask(user);

        Task saved = taskRepository.save(task);

        assertThat(saved.getId())
                .isNotNull();

        assertThat(saved.getTitle())
                .isEqualTo("Create integration tests");

        assertThat(saved.getStatus())
                .isEqualTo(TaskStatus.PENDING);

        assertThat(saved.getPriority())
                .isEqualTo(TaskPriority.HIGH);

        assertThat(saved.getCreatedAt())
                .isNotNull();
    }

    @Test
    void shouldFindTaskById() {

        User user = createUser();

        Task task = taskRepository.save(
                createTask(user)
        );

        Optional<Task> result =
                taskRepository.findById(task.getId());

        assertThat(result)
                .isPresent();

        assertThat(result.get().getTitle())
                .isEqualTo("Create integration tests");
    }

    @Test
    void shouldDeleteTask() {

        User user = createUser();

        Task task = taskRepository.save(
                createTask(user)
        );

        taskRepository.delete(task);

        Optional<Task> result =
                taskRepository.findById(task.getId());

        assertThat(result)
                .isEmpty();
    }

    private Task createTask(User user) {

        return Task.builder()
                .title("Create integration tests")
                .description("Test repository with PostgreSQL")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .dueDate(
                        LocalDateTime.now()
                                .plusDays(1)
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

        User user =
                User.builder()
                        .name("John Doe")
                        .email(
                                "john_" +
                                        System.nanoTime() +
                                        "@test.com"
                        )
                        .password("123456")
                        .role(role)
                        .build();

        return userRepository.save(user);
    }
}