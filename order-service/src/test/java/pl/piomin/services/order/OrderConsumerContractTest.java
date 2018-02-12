package pl.piomin.services.order;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { 
	"account-service.ribbon.listOfServers: localhost:8092",
	"account-service.ribbon.eureka.enabled: false",
	"eureka.client.enabled: false",
})
public class OrderConsumerContractTest {



}
