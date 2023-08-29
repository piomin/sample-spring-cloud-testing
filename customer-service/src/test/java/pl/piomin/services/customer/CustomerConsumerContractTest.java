package pl.piomin.services.customer;

import java.util.List;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.piomin.services.customer.client.AccountClient;
import pl.piomin.services.customer.model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "account-service.ribbon.listOfServers: localhost:8092",
        "account-service.ribbon.eureka.enabled: false",
        "eureka.client.enabled: false",
})
@ExtendWith(PactConsumerTestExt.class)
public class CustomerConsumerContractTest {

    @Autowired
    private AccountClient accountClient;

    @Pact(provider = "customerServiceProvider", consumer = "accountClient")
    public RequestResponsePact callAccountClient(PactDslWithProvider builder) {
        return builder.given("list-of-3-accounts").uponReceiving("test-account-service")
                .path("/customer/1").method("GET").willRespondWith().status(200)
                .body("[{\"id\":\"1\",\"number\":\"123\",\"balance\":5000},{\"id\":\"2\",\"number\":\"124\",\"balance\":5000},{\"id\":\"3\",\"number\":\"125\",\"balance\":5000}]", "application/json").toPact();
    }

    @Test
//    @PactVerification(fragment = "callAccountClient")
    public void verifyAccountsPact() {
        List<Account> accounts = accountClient.findByCustomer("1");
        assertEquals(3, accounts.size());
    }

}
