package com.well.tech.task.manager.integration.swagger;

import com.well.tech.task.manager.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should display Swagger UI page")
    void shouldDisplaySwaggerUiPage() {

        String content =
                given()
                        .port(port)
                        .when()
                        .get("/swagger-ui/index.html")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        assertThat(content)
                .contains("Swagger UI");
    }
}