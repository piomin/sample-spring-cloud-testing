package pl.piomin.services.account;

import pl.piomin.services.account.repository.AccountRepository;

//@RunWith(SpringRestPactRunner.class)
//@Provider("orderServiceProvider")
//@PactBroker(host = "192.168.99.100", port = "9080")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "eureka.client.enabled: false" })
public class Account2ProviderContractTest {
	
//	@MockBean
	private AccountRepository repository;
//	@TestTarget
//	public final Target target = new HttpTarget(8091);

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
