package com.wallet.controller;

import com.wallet.model.Account;
import com.wallet.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController //This tells Spring Boot, "This class is going to handle web requests over the internet, and it will reply with raw data (like JSON)
@RequestMapping("/api/accounts") //defines the exact URL path for this door(Accounts)

// Handing the Accountservice to the controller(called Dependency injection)//
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

@PostMapping
//This tells Spring that this specific method should only run if it receives an HTTP POST request. 
// POST is the standard internet method for creating new things (like creating a new account).

public ResponseEntity<Account> createAccount(@RequestParam String accountNumber, @RequestParam BigDecimal InitialBalance){
    Account newAccount = accountService.createAccount(accountNumber, InitialBalance);
    return ResponseEntity.ok(newAccount);
}

@GetMapping
public ResponseEntity<List<Account>> getAllAccounts(){
    List<Account> accounts = accountService.getAllAccounts();
    return ResponseEntity.ok(accounts);
}
}
