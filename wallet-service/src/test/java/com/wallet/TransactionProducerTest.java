package com.wallet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.wallet.service.TransactionProducer;


@ExtendWith(MockitoExtension.class)
public class TransactionProducerTest {
    
    @Mock 
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @InjectMocks 
    private TransactionProducer transactionProducer;
    
    @Test
    public void testSendMesssage(){
        //1.ARRANGE
        String topicName = "transaction-events";
        String testMessage = "Transfer of 50.00 successfull";

        //2.ACT
        transactionProducer.sendTransactionEvent(testMessage);

        //3. ASSERT
        // we use mockito to verify that the fake kafkaTemplate had its send() method called with our exact topic and message.
        Mockito.verify(kafkaTemplate).send(topicName, testMessage);
    }

    
}
