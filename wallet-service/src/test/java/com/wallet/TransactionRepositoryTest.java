package com.wallet;
import com.wallet.repository.*;
import com.wallet.model.*;
import com.wallet.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.*;
import java.util.Optional;
import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest 
public class TransactionRepositoryTest {
    @Autowired 
    private TransactionRepository transactionRepository;

    
    @Test
    public void testSaveTransaction(){

        //Arrange
        Transaction transaction = new Transaction();

        transaction.setSourceAccountNumber("1001");
        transaction.setTargetAccountNumber("1002");
        transaction.setAmount(new BigDecimal("50.00"));
        transaction.setTimestamp(LocalDateTime.now());

        //ACT
        Transaction savedTransaction = transactionRepository.save(transaction);
        //since the unique id is generated when saved transaction we will fetch that
        Optional<Transaction> foundTransaction=transactionRepository.findById(savedTransaction.getId());
       
        //ASSERT
        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get().getSourceAccountNumber()).isEqualTo("1001");
        assertThat(foundTransaction.get().getAmount()).isEqualByComparingTo("50.00");
    }

}
