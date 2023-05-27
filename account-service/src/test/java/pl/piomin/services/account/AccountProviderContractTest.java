package pl.piomin.services.account;

import pl.piomin.services.account.model.Account;
import pl.piomin.services.account.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

//@RunWith(SpringRestPactRunner.class)
//@Provider("customerServiceProvider")
//@PactBroker(host = "192.168.99.100", port = "9080")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "eureka.client.enabled: false" })
public class AccountProviderContractTest {

    //	@MockBean
    private AccountRepository repository;
    //	@TestTarget
//    public final Target target = new HttpTarget(8091);

    //    @State("list-of-3-accounts")
    public void toDefaultState() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("1", "123", 5000, "1"));
        accounts.add(new Account("2", "124", 5000, "1"));
        accounts.add(new Account("3", "125", 5000, "1"));
        when(repository.findByCustomerId("1")).thenReturn(accounts);
    }

}
