package pl.piomin.services.customer;

import java.util.List;

import pl.piomin.services.customer.client.AccountClient;
import pl.piomin.services.customer.model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest(properties = {
//        "account-service.ribbon.listOfServers: localhost:8092",
//        "account-service.ribbon.eureka.enabled: false",
//        "eureka.client.enabled: false",
//})
public class CustomerConsumerContractTest {

//    @Rule
//    public PactProviderRuleMk2 stubProvider = new PactProviderRuleMk2("customerServiceProvider", "localhost", 8092, this);

//    @Autowired
    private AccountClient accountClient;

//    @Pact(state = "list-of-3-accounts", provider = "customerServiceProvider", consumer = "accountClient")
//    public RequestResponsePact callAccountClient(PactDslWithProvider builder) {
//        return builder.given("list-of-3-accounts").uponReceiving("test-account-service")
//                .path("/customer/1").method("GET").willRespondWith().status(200)
//                .body("[{\"id\":\"1\",\"number\":\"123\",\"balance\":5000},{\"id\":\"2\",\"number\":\"124\",\"balance\":5000},{\"id\":\"3\",\"number\":\"125\",\"balance\":5000}]", "application/json").toPact();
//    }

//    @Test
//    @PactVerification(fragment = "callAccountClient")
    public void verifyAccountsPact() {
        List<Account> accounts = accountClient.findByCustomer("1");
        assertEquals(3, accounts.size());
    }

}
