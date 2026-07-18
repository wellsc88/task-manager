package com.well.tech.task.manager.integration.controller;

import com.well.tech.task.manager.integration.config.AbstractIntegrationTest;
import com.well.tech.task.manager.entity.Role;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.repository.RoleRepository;
import com.well.tech.task.manager.repository.UserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TaskControllerIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should create task successfully")
    void shouldCreateTask() {

        String email = createUser();

        String token = login(
                email,
                "123456"
        );

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body("""
                        {
                          "title": "Integration Test Task",
                          "description": "Testing controller integration",
                          "status": "PENDING",
                          "priority": "HIGH"
                        }
                        """)
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Integration Test Task"));
    }

    @Test
    @DisplayName("Should return unauthorized without token")
    void shouldReturnUnauthorizedWithoutToken() {

        given()
                .port(port)
                .when()
                .get("/api/tasks")
                .then()
                .statusCode(401);
    }

    private String login(
            String email,
            String password
    ) {

        return given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "%s",
                          "password": "%s"
                        }
                        """.formatted(email, password))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    private String createUser() {

        Role role =
                roleRepository.findByName("USER")
                        .orElseThrow();

        String email =
                "test_" +
                        UUID.randomUUID() +
                        "@test.com";

        User user =
                User.builder()
                        .name("Integration User")
                        .email(email)
                        .password(
                                passwordEncoder.encode("123456")
                        )
                        .role(role)
                        .build();

        userRepository.save(user);

        return email;
    }
}