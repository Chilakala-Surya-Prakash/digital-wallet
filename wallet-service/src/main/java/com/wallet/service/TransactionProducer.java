package com.wallet.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    public TransactionProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendTransactionEvent(String message){
        //"transaction-events" is the name of the kafka channel we are broadcasting to

        kafkaTemplate.send("transaction-events", message);
        System.out.println("Broadcasted to Kafka: " + message);
    }
    
}
