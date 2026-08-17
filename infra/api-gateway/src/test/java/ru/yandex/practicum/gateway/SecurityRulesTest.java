package ru.yandex.practicum.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureWebTestClient
public class SecurityRulesTest {
    @TestConfiguration
    static class TestBackendConfig {

        @Bean
        RouterFunction<ServerResponse> testBackendRoutes() {
            return route(path("/test-backend"), request -> ServerResponse.ok().build());
        }
    }

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void products_areAvailableWithoutAuthentication() {

        webTestClient
                .get()
                .uri("/api/products/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createOrder_withoutAuthentication_isUnauthorized() {
        webTestClient.post()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void writeProduct_asUser_isForbidden() {
        webTestClient.patch()
                .uri("/api/products/10")
                .headers(headers -> headers.setBasicAuth("ivan", "ivan"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void writeCategory_asUser_isForbidden() {
        webTestClient.patch()
                .uri("/api/categories/10")
                .headers(headers -> headers.setBasicAuth("ivan", "ivan"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void getOrderByEmail_asUser_isAllowed() {
        webTestClient.patch()
                .uri("/api/orders/by-email?ivan@example.com")
                .headers(headers -> headers.setBasicAuth("ivan", "ivan"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getOrders_asUser_isForbidden() {
        webTestClient.patch()
                .uri("/api/orders")
                .headers(headers -> headers.setBasicAuth("ivan", "ivan"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void getOrders_asAdmin_isAllowed() {
        webTestClient.patch()
                .uri("/api/orders")
                .headers(headers -> headers.setBasicAuth("anna", "anna"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getProduct_asUser_isAvailable() {
        webTestClient.get()
                .uri("/api/products")
                .headers(headers -> headers.setBasicAuth("ivan", "ivan"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(username = "anna", roles = "ADMIN")
    void writeProduct_asAdmin_passesSecurityCheck() {
        webTestClient.patch()
                .uri("/api/products/10")
                .headers(headers -> headers.setBasicAuth("anna", "anna"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void preflight_isNotBlockedBySecurity() {
        webTestClient.options()
                .uri("/api/orders")
                .header("Origin", "http://localhost:8443")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "authorization, content-type")
                .exchange()
                .expectStatus().isOk();
    }
}
