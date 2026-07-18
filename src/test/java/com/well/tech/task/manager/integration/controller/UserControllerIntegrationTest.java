package com.well.tech.task.manager.integration.controller;

import com.well.tech.task.manager.integration.config.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUser() {

        String email =
                "john_" + UUID.randomUUID() + "@test.com";

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "John Doe",
                          "email": "%s",
                          "password": "123456"
                        }
                        """.formatted(email))
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("John Doe"))
                .body("email", equalTo(email));
    }

    @Test
    @DisplayName("Should return bad request when payload is invalid")
    void shouldReturnBadRequestWhenPayloadInvalid() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "",
                          "email": "invalid",
                          "password": ""
                        }
                        """)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }
}