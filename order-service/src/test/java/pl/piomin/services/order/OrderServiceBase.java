package pl.piomin.services.order;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import pl.piomin.services.order.client.AccountClient;
import pl.piomin.services.order.client.CustomerClient;
import pl.piomin.services.order.client.ProductClient;
import pl.piomin.services.order.model.Account;
import pl.piomin.services.order.model.Customer;
import pl.piomin.services.order.model.CustomerType;
import pl.piomin.services.order.model.Order;
import pl.piomin.services.order.model.Product;
import pl.piomin.services.order.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	"eureka.client.enabled: false"
})
public class OrderServiceBase {

    @Autowired
    private WebApplicationContext context;
	@MockBean
    private OrderRepository repository;
	@MockBean
	private AccountClient accountClient;
	@MockBean
	private CustomerClient customerClient;
	@MockBean
	private ProductClient productClient;
	
    @Before
    public void setup() {
    	RestAssuredMockMvc.webAppContextSetup(context);
		when(repository.countByCustomerId(Matchers.anyString())).thenReturn(0);
		when(repository.save(Mockito.any(Order.class))).thenAnswer(new Answer<Order>() {
			@Override
			public Order answer(InvocationOnMock invocation) throws Throwable {
				Order o = invocation.getArgumentAt(0, Order.class);
				o.setId("12345");
				return o;
			}
		});
		when(customerClient.findByIdWithAccounts("1")).thenReturn(new Customer("1", "Test", CustomerType.REGULAR, Collections.singletonList(new Account("1", "123", 2000))));
    	when(productClient.findByIds(Arrays.asList("1", "3"))).thenReturn(Arrays.asList(new Product("1", "Test1", 500), new Product("3", "Test3", 500)));
    	when(accountClient.withdraw("1", 1000)).thenReturn(new Account("1", "123", 1000));
    }

}
