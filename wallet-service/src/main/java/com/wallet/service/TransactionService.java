package com.wallet.service;

import com.wallet.model.Account;
import com.wallet.model.Transaction;
import com.wallet.repository.AccountRepository;
import com.wallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service

public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional 

    public Transaction transferMoney(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount){
        //find both accounts

        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
        
        //does the sender have enough money for the tranaction

        if(sourceAccount.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient funds!");
        }

        //Move the money

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        //save the updated account balances

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        //create and save the permanent transaction receipt

        Transaction transaction = new Transaction();
        transaction.setSourceAccountNumber(sourceAccountNumber);
        transaction.setTargetAccountNumber(targetAccountNumber);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    
}
