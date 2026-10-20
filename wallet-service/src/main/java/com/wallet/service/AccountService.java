package com.wallet.service;
import com.wallet.model.Account;
import com.wallet.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Transactional 
    public Account createAccount(String accountNumber, BigDecimal InitialBalance){
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(InitialBalance);
        return accountRepository.save(account);
    }
    
}
