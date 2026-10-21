package com.wallet.controller;
import com.wallet.model.Transaction;
import com.wallet.service.TransactionService;
import com.wallet.service.TransactionProducer;
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
    private final TransactionProducer transactionProducer;

    public TransactionController(TransactionService transactionService, TransactionProducer transactionProducer){
        this.transactionService = transactionService;
        this.transactionProducer = transactionProducer;
    }


    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferMoney(
        @RequestParam String sourceAccountNumber,
        @RequestParam String targetAccountNumber,
        @RequestParam BigDecimal amount){
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
