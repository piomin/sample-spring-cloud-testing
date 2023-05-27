package pl.piomin.services.customer;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.services.customer.model.Customer;
import pl.piomin.services.customer.model.CustomerType;

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ActiveProfiles({"test", "no-discovery"})
public class CustomerComponentTest {

//    @Autowired
    TestRestTemplate restTemplate;

//    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule
            .inSimulationMode(dsl(
                    service("account-service:8080")
                            .andDelay(200, TimeUnit.MILLISECONDS).forAll()
                            .get(startsWith("/customer/"))
                            .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":5000}]", "application/json"))))
            .printSimulationData();

//    @Test
    public void testAdd() {
        Customer customer = new Customer("Test1", CustomerType.REGULAR);
        customer = restTemplate.postForObject("/", customer, Customer.class);
        assertNotNull(customer);
    }

//    @Test
    public void testFindWithAccounts() {
        Customer customer = new Customer("Test2", CustomerType.REGULAR);
        customer = restTemplate.postForObject("/", customer, Customer.class);
//		customer = restTemplate.getForObject("/withAccounts/{id}", Customer.class, customer.getId());
        assertNotNull(customer);
//		Assert.assertNotNull(customer.getAccounts());
//		Assert.assertEquals(1, customer.getAccounts().size());
    }
}
