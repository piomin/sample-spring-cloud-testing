package pl.piomin.services.account;

import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import pl.piomin.services.account.model.Account;
import pl.piomin.services.account.repository.AccountRepository;

//@RunWith(SpringRestPactRunner.class)
//@Provider("orderServiceProvider")
//@PactBroker(host = "192.168.99.100", port = "9080")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "eureka.client.enabled: false" })
public class Account2ProviderContractTest {
	
//	@MockBean
	private AccountRepository repository;
//	@TestTarget
	public final Target target = new HttpTarget(8091);

//    @State("withdraw-from-account")
    public void toDefaultState() {
//        when(repository.findById("1")).thenReturn(new Account("1", "123", 5000, "1"));
//		when(repository.save(Mockito.any(Account.class))).thenAnswer(new Answer<Account>() {
//			@Override
//			public Account answer(InvocationOnMock invocation) throws Throwable {
//				Account a = invocation.getArgumentAt(0, Account.class);
//				return a;
//			}
//		});
    }
	
}
