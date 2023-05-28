package pl.piomin.services.customer;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.customer.model.Customer;
import pl.piomin.services.customer.model.CustomerType;

import java.util.concurrent.TimeUnit;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "no-discovery"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(HoverflyExtension.class)
public class CustomerComponentTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:5.0");
    static String id;

    @Test
    @Order(1)
    public void testAdd() {
        Customer customer = new Customer("Test1", CustomerType.REGULAR);
        customer = restTemplate.postForObject("/", customer, Customer.class);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertEquals("Test1", customer.getName());
        id = customer.getId();
    }

    @Test
    @Order(2)
    void testFindById() {
        Customer c = restTemplate.getForObject("/{id}", Customer.class, id);
        assertNotNull(c);
        assertNotNull(c.getId());
        assertEquals(id, c.getId());
    }

    @Test
    @Order(2)
    public void testFindWithAccounts(Hoverfly hoverfly) {
        hoverfly.simulate(dsl(
                service("account-service")
                        .andDelay(200, TimeUnit.MILLISECONDS).forAll()
                        .get(startsWith("/customer/"))
                        .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":5000}]", "application/json"))));
        Customer customer = new Customer("Test2", CustomerType.REGULAR);
//        customer = restTemplate.postForObject("/", customer, Customer.class);
		customer = restTemplate.getForObject("/withAccounts/{id}", Customer.class, id);
        assertNotNull(customer);
		assertNotNull(customer.getAccounts());
//		assertEquals(1, customer.getAccounts().size());
//        assertEquals("1", customer.getAccounts().get(0).getId());
    }
}
