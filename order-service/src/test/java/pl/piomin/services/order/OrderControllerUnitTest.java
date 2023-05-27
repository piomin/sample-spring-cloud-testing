package pl.piomin.services.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.piomin.services.order.client.AccountClient;
import pl.piomin.services.order.client.CustomerClient;
import pl.piomin.services.order.client.ProductClient;
import pl.piomin.services.order.controller.OrderController;
import pl.piomin.services.order.model.*;
import pl.piomin.services.order.repository.OrderRepository;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerUnitTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;
    @MockBean
    OrderRepository repository;
    @MockBean
    AccountClient accountClient;
    @MockBean
    CustomerClient customerClient;
    @MockBean
    ProductClient productClient;

    @Test
    public void testAccept() throws Exception {
        Order order = new Order("1", OrderStatus.ACCEPTED, 2000, "1", "1", null);
        when(repository.findById("1")).thenReturn(Optional.of(order));
        when(accountClient.withdraw(order.getAccountId(), order.getPrice())).thenReturn(new Account("1", "123", 0));
        when(repository.save(Mockito.any(Order.class))).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order o = invocation.getArgument(0, Order.class);
                return o;
            }
        });
        mvc.perform(put("/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("DONE")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPrepare() throws Exception {
        Order order = new Order(null, OrderStatus.NEW, 0, "1", "1", Collections.singletonList("1"));
        when(productClient.findByIds(Mockito.anyList())).thenReturn(Collections.singletonList(new Product(order.getProductIds().get(0), "Test", 1000)));
        when(customerClient.findByIdWithAccounts(order.getCustomerId())).thenReturn(new Customer(order.getCustomerId(), "Test", CustomerType.REGULAR, Collections.singletonList(new Account(order.getAccountId(), "123", 2000))));
        when(repository.countByCustomerId(order.getCustomerId())).thenReturn(0);
        when(repository.save(Mockito.any(Order.class))).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order o = invocation.getArgument(0, Order.class);
                o.setId("1");
                return o;
            }
        });
        mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("ACCEPTED")))
                .andExpect(jsonPath("$.price", is(950)));
    }

}
