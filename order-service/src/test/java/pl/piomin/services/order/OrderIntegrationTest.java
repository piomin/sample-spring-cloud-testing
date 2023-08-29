package pl.piomin.services.order;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflySimulate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.piomin.services.order.client.AccountClient;
import pl.piomin.services.order.client.CustomerClient;
import pl.piomin.services.order.client.ProductClient;
import pl.piomin.services.order.model.Account;
import pl.piomin.services.order.repository.OrderRepository;

@SpringBootTest
@ActiveProfiles("dev")
@Tag("integration")
@ExtendWith(HoverflyExtension.class)
@HoverflySimulate(source = @HoverflySimulate.Source("account.json"), enableAutoCapture = true)
public class OrderIntegrationTest {

    @Autowired
    AccountClient accountClient;
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void testAccount() {
        Account account = accountClient.add(new Account(null, "123", 5000));
        account = accountClient.withdraw(account.getId(), 1000);
        System.out.println(account);
    }

}
