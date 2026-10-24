package com.wallet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.wallet.controller.TransactionController;
import com.wallet.model.Transaction;
import com.wallet.service.TransactionProducer;
import com.wallet.service.TransactionService;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simulates HTTP client requests

    @MockBean
    private TransactionService transactionService; // Mocked dependency to isolate controller logic

    @MockBean
    private TransactionProducer transactionProducer; // Our Fake Kafka Megaphone

    // Add this to satisfy the IdempotencyFilter!
    @MockBean
    private org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate;
    @Test
    public void testSuccessfulTransferRequest() throws Exception {
        // 1. Arrange: Initialize mock data and stub the service behavior
        Transaction mockTransaction = new Transaction();
        mockTransaction.setSourceAccountNumber("1001");
        mockTransaction.setTargetAccountNumber("1002");
        mockTransaction.setAmount(new BigDecimal("25.50"));

        when(transactionService.transferMoney(eq("1001"), eq("1002"),any(BigDecimal.class) )).thenReturn(mockTransaction);

        //2. ACT& 3. ASSERT: Execute the Http post request and verify the JSON response 
        mockMvc.perform(post("/api/transactions/transfer")
            .param("sourceAccountNumber", "1001")
            .param("targetAccountNumber", "1002")
            .param("amount","25.00"))
            .andExpect(status().isOk())//Asserts HTTP 200 status
            .andExpect(jsonPath("$.sourceAccountNumber").value("1001"))
            .andExpect(jsonPath("$.amount").value(25.5));

    }
    @Test
    public void testTransferRequestWithNegativeAmount() throws Exception {
        
        // 1. ARRANGE: No stubbing needed! 
        // The Controller should block this request before it ever reaches our Mocked Service.

        // 2. ACT & 3. ASSERT: Send a request with a negative amount and expect a 400 Bad Request
        mockMvc.perform(post("/api/transactions/transfer")
                .param("sourceAccountNumber", "1001")
                .param("targetAccountNumber", "1002")
                .param("amount", "-50.00"))
                .andExpect(status().isBadRequest()); // The Proof: Did the Controller reject it?
    
    }

}