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
class AuthControllerIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PASSWORD = "123456";

    @Test
    @DisplayName("Should authenticate user successfully")
    void shouldLoginSuccessfully() {

        String email = createUser();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "%s",
                          "password": "%s"
                        }
                        """.formatted(email, PASSWORD))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Should return unauthorized when password is invalid")
    void shouldReturnUnauthorizedWhenPasswordInvalid() {

        String email = createUser();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "%s",
                          "password": "wrong-password"
                        }
                        """.formatted(email))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should return unauthorized when user does not exist")
    void shouldReturnUnauthorizedWhenUserNotFound() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "email": "notfound@test.com",
                          "password": "123456"
                        }
                        """)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401);
    }

    private String createUser() {

        Role role =
                roleRepository.findByName("USER")
                        .orElseThrow();

        String email =
                "auth_" +
                        UUID.randomUUID() +
                        "@test.com";

        User user =
                User.builder()
                        .name("Auth Test User")
                        .email(email)
                        .password(
                                passwordEncoder.encode(PASSWORD)
                        )
                        .role(role)
                        .enabled(true)
                        .build();

        userRepository.save(user);

        return email;
    }
}