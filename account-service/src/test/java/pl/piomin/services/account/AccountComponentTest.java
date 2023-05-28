package pl.piomin.services.account;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.account.model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "no-discovery"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountComponentTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:5.0");
    static String id;

    @Test
    @Order(1)
    void testAdd() {
        Account account = new Account("1234567890", 5000, "1");
        account = restTemplate.postForObject("/", account, Account.class);
        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals("1234567890", account.getNumber());
        id = account.getId();
    }

    @Test
    @Order(2)
    void testFindById() {
        Account a = restTemplate.getForObject("/{id}", Account.class, id);
        assertNotNull(a);
        assertNotNull(a.getId());
        assertEquals(id, a.getId());
    }

    @Test
    @Order(2)
    void testFindByCustomerId() {
        Account[] a = restTemplate.getForObject("/customer/{id}", Account[].class, "1");
        assertNotNull(a);
        assertEquals(1, a.length);
        assertNotNull(a[0].getId());
    }
}
