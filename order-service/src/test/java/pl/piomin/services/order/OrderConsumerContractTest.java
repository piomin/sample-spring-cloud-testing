package pl.piomin.services.order;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.piomin.services.order.client.AccountClient;
import pl.piomin.services.order.model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
	"account-service.ribbon.listOfServers: localhost:8092",
	"account-service.ribbon.eureka.enabled: false",
	"eureka.client.enabled: false",
})
@ExtendWith(PactConsumerTestExt.class)
public class OrderConsumerContractTest {

    @Autowired
    private AccountClient accountClient;

    @Pact(provider = "orderServiceProvider", consumer = "accountClient")
    public RequestResponsePact callAccountClient(PactDslWithProvider builder) {
        return builder.given("withdraw-from-account").uponReceiving("test-account-service")
                .path("/withdraw/1/1000").method("PUT").willRespondWith().status(200)
                .body("{\"id\":\"1\",\"number\":\"123\",\"balance\":4000}", "application/json").toPact();
    }

    @Test
//	@PactVerification(fragment = "callAccountClient")
    public void verifyAccountWithrawPact() {
        Account account = accountClient.withdraw("1", 1000);
        assertNotNull(account);
        assertEquals(4000, account.getBalance());
        assertEquals("1", account.getId());
    }

}
