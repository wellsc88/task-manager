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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AdminUserControllerIntegrationTest extends AbstractIntegrationTest {

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
    @DisplayName("Should list users when authenticated as admin")
    void shouldListUsersAsAdmin() {

        String adminEmail = createAdminUser();

        String token =
                login(
                        adminEmail,
                        PASSWORD
                );


        given()
                .port(port)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .when()
                .get("/api/admin/users")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Should return forbidden when user is not admin")
    void shouldReturnForbiddenWhenUserIsNotAdmin() {

        User user = createNormalUser();

        String token = login(
                user.getEmail(),
                PASSWORD
        );

        given()
                .port(port)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .when()
                .get("/api/admin/users")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should update user status as admin")
    void shouldUpdateUserStatus() {

        String adminEmail = createAdminUser();

        createAdminUser();

        User user = createNormalUser();

        String token = login(adminEmail, PASSWORD);

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body("""
                        {
                          "enabled": false
                        }
                        """)
                .when()
                .patch(
                        "/api/admin/users/%s/status"
                                .formatted(user.getId())
                )
                .then()
                .statusCode(204);

        User updated =
                userRepository.findById(user.getId())
                        .orElseThrow();

        assertThat(updated.isEnabled())
                .isFalse();
    }

    private String createAdminUser() {

        Role role =
                roleRepository.findByName("ADMIN")
                        .orElseThrow();

        String email =
                "admin_" +
                        UUID.randomUUID() +
                        "@test.com";


        User user =
                User.builder()
                        .name("Admin Test")
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

    private User createNormalUser() {

        Role role =
                roleRepository.findByName("USER")
                        .orElseThrow();

        User user =
                User.builder()
                        .name("User Test")
                        .email(
                                UUID.randomUUID()
                                        + "@test.com"
                        )
                        .password(
                                passwordEncoder.encode(PASSWORD)
                        )
                        .role(role)
                        .enabled(true)
                        .build();

        return userRepository.save(user);
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
}