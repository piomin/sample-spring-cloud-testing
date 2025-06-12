package pl.piomin.services.account;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.piomin.services.account.model.Account;
import pl.piomin.services.account.repository.AccountRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Provider("orderServiceProvider")
@PactBroker(host = "localhost", port = "9292")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"eureka.client.enabled: false"})
public class Account2ProviderContractTest {

    @MockitoBean
    private AccountRepository repository;

    @BeforeEach
    void before(PactVerificationContext context) {
        HttpTestTarget testTarget = new HttpTestTarget("localhost:8091");
        context.setTarget(testTarget);
    }

    @State("withdraw-from-account")
    public void toDefaultState() {
        when(repository.findById("1")).thenReturn(Optional.of(new Account("1", "123", 5000, "1")));
		when(repository.save(Mockito.any(Account.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Account answer(InvocationOnMock invocation) throws Throwable {
				Account a = invocation.getArgument(0, Account.class);
				return a;
			}
		});
    }

}
