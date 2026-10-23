package com.wallet;

import com.wallet.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.wallet.service.TransactionService;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import com.wallet.repository.AccountRepository;
import com.wallet.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Tell junit to enable Mockito for this test class
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

// Mocked Dependencies(The fakes)
@Mock
private AccountRepository accountRepository;

@Mock
private TransactionRepository transactionRepository;

//Inject mocks into the target service
@InjectMocks 
private TransactionService transactionService;


@Test 
  // every good test is broken down into exactly three steps. We call this the "Given, When, Then" rule:

    //GIVEN (The Setup): We tell our Fake Fridge what food is inside it today.

    //WHEN (The Action): We tell the Chef to cook the meal (run the transfer).

    //THEN (The Verification): We taste the food to make sure it's right.

    public void testSuccessfulTransfer() {
        // 1. GIVEN (The Setup)
        // Here we will create two fake accounts and put them in the fake database.

        Account sender = new Account();
        sender.setAccountNumber("1001");
        sender.setBalance(new BigDecimal("100.00"));

        Account receiver = new Account();
        receiver.setAccountNumber("1002");
        receiver.setBalance(new BigDecimal("50.00"));
//Training a Mock

        when(accountRepository.findByAccountNumber("1001")).thenReturn(sender);
        when(accountRepository.findByAccountNumber("1002")).thenReturn(receiver);

        // 2. WHEN (The Action)
        // Here we will tell the TransactionService to transfer $10 from Account 1 to Account 2.
        
        transactionService.transferMoney("1001", "1002",new BigDecimal("10.00"));


        // 3. THEN (The Verification)
        //In testing, we verify things using a tool called an Assertion. We are going to use a command called assertEquals(expected, actual).
        //It translates to: "Hey Java, I expect the answer to be X. Check the actual account, and if it's not X, fail the test!"
        // Here we will check if Account 1 lost $10, and Account 2 gained $10.

        //check if sender has $90.00
        assertEquals(new BigDecimal("90.00"), sender.getBalance() );
        //check if reciever has $60.00
        assertEquals(new BigDecimal("60.00"), receiver.getBalance());
    }

    @Test
    public void InsufficientFunds(){
        //Given
        Account sender = new Account();
        sender.setAccountNumber("1001");
        sender.setBalance(new BigDecimal("50.00"));

        Account reciever = new Account();
        reciever.setAccountNumber("1002");
        reciever.setBalance(new BigDecimal("50"));
 //Training a Mock

        when(accountRepository.findByAccountNumber("1001")).thenReturn(sender);
        when(accountRepository.findByAccountNumber("1002")).thenReturn(reciever);

        // 2. WHEN (The Action)& THEN
        //If it throws a RuntimeException when I try to transfer $100, count this test as a PASSED test!"
        assertThrows(RuntimeException.class, () ->{
        transactionService.transferMoney("1001", "1002", new BigDecimal("100.00"));
        });
       
    }

}
