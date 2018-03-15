package pl.piomin.services.product;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import pl.piomin.services.product.model.Product;
import pl.piomin.services.product.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProductApplication.class}, properties = { "eureka.client.enabled: false" })
public class ProductServiceBase {

    @Autowired
    private WebApplicationContext context;
	@MockBean
    private ProductRepository repository;
	
    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
		final List<Product> products = new ArrayList<>();
		products.add(new Product("1", "Test1", 500));
		products.add(new Product("2", "Test2", 300));
		products.add(new Product("3", "Test3", 800));
		products.add(new Product("4", "Test4", 200));
		products.add(new Product("5", "Test5", 500));
		products.add(new Product("6", "Test6", 700));
        when(repository.findByIdIn(Mockito.anyListOf(String.class))).thenAnswer(new Answer<List<Product>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<Product> answer(InvocationOnMock invocation) throws Throwable {
				List<String> ids = invocation.getArgumentAt(0, List.class);
				return products.stream().filter(it -> ids.contains(it.getId())).collect(Collectors.toList());
			}
		});
    }
    
}
