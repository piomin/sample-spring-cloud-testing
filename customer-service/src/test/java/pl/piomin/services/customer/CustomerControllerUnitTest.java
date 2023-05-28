package pl.piomin.services.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.piomin.services.customer.client.AccountClient;
import pl.piomin.services.customer.controller.CustomerController;
import pl.piomin.services.customer.model.Account;
import pl.piomin.services.customer.model.Customer;
import pl.piomin.services.customer.model.CustomerType;
import pl.piomin.services.customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerUnitTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;
    @MockBean
    CustomerRepository repository;
    @MockBean
    AccountClient accountClient;

    @Test
    void testAdd() throws Exception {
        Customer c = new Customer("Test1", CustomerType.NEW);
        when(repository.save(Mockito.any(Customer.class)))
                .thenReturn(new Customer("1", "Test1", CustomerType.NEW));
        mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")));
    }

    @Test
    void testFindById() throws Exception {
        Customer c = new Customer("1", "Test1", CustomerType.NEW);
        when(repository.findById("1")).thenReturn(Optional.of(c));
        mvc.perform(get("/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test1")));
    }

    @Test
    void testFindByIdWithAccounts() throws Exception {
        Customer c = new Customer("1", "Test1", CustomerType.NEW);
        when(repository.findById("1")).thenReturn(Optional.of(c));
        Account account = new Account("1", "1234567890", 5000);
        Account account2 = new Account("2", "1234567891", 15000);
        when(accountClient.findByCustomer("1")).thenReturn(List.of(account, account2));
        mvc.perform(get("/withAccounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accounts.length()", is(2)));
    }
}
