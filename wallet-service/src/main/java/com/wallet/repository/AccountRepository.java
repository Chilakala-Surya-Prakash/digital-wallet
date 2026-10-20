package com.wallet.repository;

import com.wallet.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface AccountRepository extends JpaRepository<Account, Long> {
    //spring boot will automatically write the sql query to find an account by its number 
    Account findByAccountNumber(String accountNumber);
}
