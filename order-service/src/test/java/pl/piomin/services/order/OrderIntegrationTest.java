package pl.piomin.services.order;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.services.order.client.AccountClient;
import pl.piomin.services.order.client.CustomerClient;
import pl.piomin.services.order.client.ProductClient;
import pl.piomin.services.order.model.Account;
import pl.piomin.services.order.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Category(IntegrationTest.class)
public class OrderIntegrationTest {

	
	@Autowired
	AccountClient accountClient;
	@Autowired
	CustomerClient customerClient;
	@Autowired
	ProductClient productClient;
	@Autowired
	OrderRepository orderRepository;
	
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inCaptureOrSimulationMode("account.json").printSimulationData();
    
	@Test
	public void testAccount() {
		Account account = accountClient.add(new Account(null, "123", 5000));
		account = accountClient.withdraw(account.getId(), 1000);
		System.out.println(account);
	}
	
}
