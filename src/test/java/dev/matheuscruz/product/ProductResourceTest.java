package dev.matheuscruz.product;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;

@QuarkusTest
class ProductResourceTest {

    @Inject
    ProductCreatedHandler handler;

    @Test
    void should_send_message_correctly() {
        RestAssured.given()
                .when()
                .post("/products")
                .then()
                .statusCode(202);

        Awaitility.await()
                .during(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(200))
                .until(() -> !handler.getEvents().isEmpty());
    }
}