package com.well.tech.task.manager.integration.repository.specification;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import com.well.tech.task.manager.integration.config.AbstractPostgresContainer;
import com.well.tech.task.manager.dto.request.TaskFilterRequest;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.Task;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.TaskRepository;
import com.well.tech.task.manager.repository.UserRepository;
import com.well.tech.task.manager.repository.specification.TaskSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class TaskSpecificationIntegrationTest extends AbstractPostgresContainer {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFilterTasksByStatus() {

        User user = createUser();

        taskRepository.save(
                createTask(
                        "Task pending",
                        TaskStatus.PENDING,
                        TaskPriority.HIGH,
                        user
                )
        );

        taskRepository.save(
                createTask(
                        "Task completed",
                        TaskStatus.COMPLETED,
                        TaskPriority.LOW,
                        user
                )
        );

        TaskFilterRequest filter =
                new TaskFilterRequest();

        filter.setStatus(TaskStatus.PENDING);

        List<Task> result =
                taskRepository.findAll(
                        TaskSpecification.filter(
                                filter,
                                user.getId()
                        )
                );

        assertThat(result)
                .hasSize(1);

        assertThat(result.getFirst().getStatus())
                .isEqualTo(TaskStatus.PENDING);
    }

    @Test
    void shouldFilterByPriority() {

        User user = createUser();

        taskRepository.save(
                Task.builder()
                        .title("Task High")
                        .priority(TaskPriority.HIGH)
                        .status(TaskStatus.PENDING)
                        .user(user)
                        .build()
        );

        taskRepository.save(
                Task.builder()
                        .title("Task Low")
                        .priority(TaskPriority.LOW)
                        .status(TaskStatus.PENDING)
                        .user(user)
                        .build()
        );

        TaskFilterRequest filter = new TaskFilterRequest();
        filter.setPriority(TaskPriority.HIGH);

        List<Task> result =
                taskRepository.findAll(
                        TaskSpecification.filter(filter, user.getId())
                );

        assertThat(result)
                .hasSize(1);

        assertThat(result.getFirst().getPriority())
                .isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void shouldFilterByTitle() {

        User user = createUser();

        taskRepository.save(
                Task.builder()
                        .title("Spring Boot API")
                        .description("Task 1")
                        .status(TaskStatus.PENDING)
                        .priority(TaskPriority.HIGH)
                        .user(user)
                        .build()
        );

        taskRepository.save(
                Task.builder()
                        .title("Docker Compose")
                        .description("Task 2")
                        .status(TaskStatus.PENDING)
                        .priority(TaskPriority.HIGH)
                        .user(user)
                        .build()
        );

        TaskFilterRequest filter = new TaskFilterRequest();
        filter.setTitle("spring");

        List<Task> tasks = taskRepository.findAll(
                TaskSpecification.filter(filter, user.getId())
        );

        assertThat(tasks)
                .hasSize(1);

        assertThat(tasks.getFirst().getTitle())
                .isEqualTo("Spring Boot API");
    }

    @Test
    void shouldIgnorePriorityWhenNull() {

        User user = createUser();
        createTasks(user);

        TaskFilterRequest filter = new TaskFilterRequest();
        filter.setPriority(null);

        List<Task> tasks = taskRepository.findAll(
                TaskSpecification.filter(filter, user.getId())
        );

        assertThat(tasks).hasSize(2);
    }

    @Test
    void shouldIgnoreTitleWhenNull() {

        User user = createUser();
        createTasks(user);

        TaskFilterRequest filter = new TaskFilterRequest();
        filter.setTitle(null);

        List<Task> tasks = taskRepository.findAll(
                TaskSpecification.filter(filter, user.getId())
        );

        assertThat(tasks).hasSize(2);
    }

    @Test
    void shouldIgnoreBlankTitle() {

        User user = createUser();
        createTasks(user);

        TaskFilterRequest filter = new TaskFilterRequest();
        filter.setTitle("   ");

        List<Task> tasks = taskRepository.findAll(
                TaskSpecification.filter(filter, user.getId())
        );

        assertThat(tasks).hasSize(2);
    }

    private Task createTask(
            String title,
            TaskStatus status,
            TaskPriority priority,
            User user
    ) {

        return Task.builder()
                .title(title)
                .description("test")
                .status(status)
                .priority(priority)
                .user(user)
                .build();
    }

    private void createTasks(User user) {

        taskRepository.save(
                Task.builder()
                        .title("Spring Boot")
                        .status(TaskStatus.PENDING)
                        .priority(TaskPriority.HIGH)
                        .user(user)
                        .build()
        );

        taskRepository.save(
                Task.builder()
                        .title("Docker")
                        .status(TaskStatus.COMPLETED)
                        .priority(TaskPriority.LOW)
                        .user(user)
                        .build()
        );
    }

    private User createUser() {

        Role role =
                roleRepository.findByName("USER")
                        .orElseThrow();

        return userRepository.save(
                User.builder()
                        .name("John")
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