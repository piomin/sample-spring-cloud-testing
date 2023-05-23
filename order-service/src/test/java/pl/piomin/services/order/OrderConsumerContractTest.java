package pl.piomin.services.order;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import pl.piomin.services.order.client.AccountClient;
import pl.piomin.services.order.model.Account;

//@RunWith(SpringRunner.class)
//@SpringBootTest(properties = {
//	"account-service.ribbon.listOfServers: localhost:8092",
//	"account-service.ribbon.eureka.enabled: false",
//	"eureka.client.enabled: false",
//})
public class OrderConsumerContractTest {

    //	@Rule
    public PactProviderRuleMk2 stubProvider = new PactProviderRuleMk2("orderServiceProvider", "localhost", 8092, this);

    @Autowired
    private AccountClient accountClient;

    //	@Pact(state = "withdraw-from-account", provider = "orderServiceProvider", consumer = "accountClient")
    public RequestResponsePact callAccountClient(PactDslWithProvider builder) {
        return builder.given("withdraw-from-account").uponReceiving("test-account-service")
                .path("/withdraw/1/1000").method("PUT").willRespondWith().status(200)
                .body("{\"id\":\"1\",\"number\":\"123\",\"balance\":4000}", "application/json").toPact();
    }

    //	@Test
//	@PactVerification(fragment = "callAccountClient")
    public void verifyAccountWithrawPact() {
        Account account = accountClient.withdraw("1", 1000);
        Assert.assertNotNull(account);
        Assert.assertEquals(4000, account.getBalance());
        Assert.assertEquals("1", account.getId());
    }

}
