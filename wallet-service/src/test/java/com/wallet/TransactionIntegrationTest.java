package com.wallet;

import com.wallet.model.Account;
import com.wallet.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();

        Account source = new Account();
        source.setAccountNumber("1001");
        source.setBalance(new BigDecimal("500.00"));
        accountRepository.save(source);

        Account target = new Account();
        target.setAccountNumber("1002");
        target.setBalance(new BigDecimal("100.00"));
        accountRepository.save(target);
    }

    @Test
    public void testCreateTransactionIntegration() throws Exception {
        mockMvc.perform(post("/api/transactions/transfer")
                .param("sourceAccountNumber", "1001")
                .param("targetAccountNumber", "1002")
                .param("amount", "75.00"))
                .andExpect(status().isOk());
    }
}