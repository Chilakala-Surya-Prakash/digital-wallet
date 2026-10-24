package com.wallet.controller;
import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.model.Transaction;
import com.wallet.service.TransactionProducer;
import com.wallet.service.TransactionService;

@RestController 
@RequestMapping("/api/transactions")

public class TransactionController {
     
    private final TransactionService transactionService;
    private final TransactionProducer transactionProducer;

    public TransactionController(TransactionService transactionService, TransactionProducer transactionProducer){
        this.transactionService = transactionService;
        this.transactionProducer = transactionProducer;
    }


    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(
        @RequestParam String sourceAccountNumber,
        @RequestParam String targetAccountNumber,
        @RequestParam BigDecimal amount){

            // Validation Block
            if(amount.compareTo(BigDecimal.ZERO)<=0){
                return ResponseEntity.badRequest().body("Amount must be greater than zero.");
            }
            
            //move the money in the database
            Transaction transaction = transactionService.transferMoney(sourceAccountNumber, targetAccountNumber, amount);

            //Mask the account numbers for security 
            String maskedSource = "****" + sourceAccountNumber.substring(Math.max(0, sourceAccountNumber.length()-4));
            String maskedTarget = "****" + targetAccountNumber.substring(Math.max(0, targetAccountNumber.length()-4));

            //Create the broadcast message
            String message = "Transfer successful:$" + amount + " from " + maskedSource + "to" + maskedTarget;
            
            //shout the message into kafka
            transactionProducer.sendTransactionEvent(message);
            
            
            return ResponseEntity.ok(transaction);
    
}

}
