package pl.piomin.services.order;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import pl.piomin.services.order.model.Order;
import pl.piomin.services.order.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	"eureka.client.enabled: false"
})
@AutoConfigureStubRunner(ids = {
		"pl.piomin.services:account-service:+:stubs:8091",
		"pl.piomin.services:customer-service:+:stubs:8092",
		"pl.piomin.services:product-service:+:stubs:8093"
}, workOffline = true)
public class OrderScenarioBase {

    @Autowired
    private WebApplicationContext context;
	@MockBean
    private OrderRepository repository;
	
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
    }

}
