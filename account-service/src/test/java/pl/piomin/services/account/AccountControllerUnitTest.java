package pl.piomin.services.account;

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
import pl.piomin.services.account.controller.AccountController;
import pl.piomin.services.account.model.Account;
import pl.piomin.services.account.repository.AccountRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerUnitTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;
    @MockBean
    AccountRepository repository;

    @Test
    public void testAdd() throws Exception {
        Account account = new Account("1234567890", 5000, "1");
        when(repository.save(Mockito.any(Account.class))).thenReturn(new Account("1", "1234567890", 5000, "1"));
        mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(account)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFind() throws Exception {
        Account account = new Account("1", "1234567890", 5000, "1");
        when(repository.findById("1")).thenReturn(Optional.of(account));
        mvc.perform(get("/1").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(account)))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithdraw() throws Exception {
        Account account = new Account("1", "1234567890", 5000, "1");
        when(repository.findById("1")).thenReturn(Optional.of(account));
        when(repository.save(Mockito.any(Account.class))).thenAnswer(new Answer<Account>() {
            @Override
            public Account answer(InvocationOnMock invocation) throws Throwable {
                Account a = invocation.getArgument(0, Account.class);
                return a;
            }
        });
        mvc.perform(put("/withdraw/1/1000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.balance", is(4000)));
    }

}
