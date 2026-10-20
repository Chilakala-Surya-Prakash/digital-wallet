package com.wallet.controller;
import com.wallet.model.Transaction;
import com.wallet.service.TransactionService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController 
@RequestMapping("/api/transactions")

public class TransactionController {
     
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }


    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferMoney(
        @RequestParam String sourceAccountNumber,
        @RequestParam String targetAccountNumber,
        @RequestParam BigDecimal amount){
            Transaction transaction = transactionService.transferMoney(sourceAccountNumber, targetAccountNumber, amount);
            return ResponseEntity.ok(transaction);
    
}

}
