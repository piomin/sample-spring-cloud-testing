package pl.piomin.services.account;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import pl.piomin.services.account.model.Account;
import pl.piomin.services.account.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AccountApplication.class}, properties = { "eureka.client.enabled: false" })
public abstract class AccountServiceBase {
	
    @Autowired
    private WebApplicationContext context;
	@MockBean
    private AccountRepository repository;
	
    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
		List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("12345", "123", 5000, "1"));
        accounts.add(new Account("12346", "124", 5000, "1"));
        accounts.add(new Account("12347", "125", 5000, "1"));
        when(repository.findByCustomerId("1")).thenReturn(accounts);
    }
    
}
