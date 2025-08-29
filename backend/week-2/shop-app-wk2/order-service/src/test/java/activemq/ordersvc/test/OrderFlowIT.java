package activemq.ordersvc.test;

import activemq.ordersvc.messaging.OrderCreatedListener;
import activemq.ordersvc.repo.OrderRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderFlowIT {

    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("order-test")
            .withUsername("order-test")
            .withPassword("order-test");

    static final GenericContainer<?> genericContainer = new GenericContainer<>("vromero/activemq-artemis:2.31.2-alpine").
            withEnv("ARTEMIS_USER", "admin")
            .withEnv("ARTEMIS_PASSWORD","admin")
            .withEnv("DISABLE_SECURITY","false")
            .withExposedPorts(61616,61616);

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        registry.add("spring.artemis.broker-url", () -> "tcp://" + genericContainer.getHost() + ":" + genericContainer.getMappedPort(61616));

        registry.add("spring.artemis.user", () -> "admin" );
        registry.add("spring.artemis.password", () -> "admin");

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    @LocalServerPort
    private int port;
    final TestRestTemplate restTemplate = new TestRestTemplate();

    // Spy
    @SpyBean
    OrderCreatedListener orderCreatedListener;
    @SpyBean
    OrderRespository orderRespository;

    @Test
    void endToEnd_order_isPersisted_andEventConsumed() {
        var req = Map.of("customerName", "Thang", "total", 120.22);
        var resp = restTemplate.postForEntity("http://localhost:" + port + "/api/orders", req, Map.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(orderRespository.count()).isGreaterThanOrEqualTo(1);
        verify(orderCreatedListener, timeout(5000)).handleOrderCreated(any());
    }
}
