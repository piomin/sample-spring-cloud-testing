package pl.piomin.services.order;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;

import java.util.Collections;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.services.order.model.Order;
import pl.piomin.services.order.model.OrderStatus;
import pl.piomin.services.order.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles({"test", "no-discovery"})
public class OrderComponentTest {

	@Autowired
	TestRestTemplate restTemplate;
	@Autowired
	OrderRepository orderRepository;
	
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule
            .inSimulationMode(dsl(
            	service("account-service:8080")
            		.put(startsWith("/withdraw/"))
            		.willReturn(success("{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":5000}", "application/json")),
            	service("customer-service:8080")
            		.get("/withAccounts/1")
            		.willReturn(success("{\"id\":\"{{ Request.Path.[1] }}\",\"name\":\"Test1\",\"type\":\"REGULAR\",\"accounts\":[{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":5000}]}", "application/json")),
            	service("product-service:8080")
            		.post("/ids").anyBody()
            		.willReturn(success("[{\"id\":\"1\",\"name\":\"Test1\",\"price\":1000}]", "application/json"))))
            .printSimulationData();
    
	@Test
	public void testAccept() {
		Order order = new Order(null, OrderStatus.ACCEPTED, 1000, "1", "1", Collections.singletonList("1"));
		order = orderRepository.save(order);
		restTemplate.put("/{id}", null, order.getId());
		order = orderRepository.findOne(order.getId());
		Assert.assertEquals(OrderStatus.DONE, order.getStatus());
	}

	@Test
	public void testPrepare() {
		Order order = new Order(null, OrderStatus.NEW, 1000, "1", "1", Collections.singletonList("1"));
		order = restTemplate.postForObject("/", order, Order.class);
		Assert.assertNotNull(order);
		Assert.assertEquals(OrderStatus.ACCEPTED, order.getStatus());
		Assert.assertEquals(940, order.getPrice());
	}
	
}
