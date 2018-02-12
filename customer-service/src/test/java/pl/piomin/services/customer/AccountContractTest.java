package pl.piomin.services.customer;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

import pl.piomin.services.customer.client.AccountClient;
import pl.piomin.services.customer.model.Account;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	"eureka.client.enabled: false",
})
@AutoConfigureStubRunner(ids = {"pl.piomin.services:account-service:+:stubs:8091"}, workOffline = true)
public class AccountContractTest {

	@Autowired
	private AccountClient accountClient;

	@Test
	public void verifyAccounts() {
		List<Account> accounts = accountClient.findByCustomer("1");
		Assert.assertEquals(3, accounts.size());
	}

}
