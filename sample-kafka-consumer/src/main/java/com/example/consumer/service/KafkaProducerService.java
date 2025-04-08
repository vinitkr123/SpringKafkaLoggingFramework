package com.example.consumer.service;

import com.example.consumer.model.KafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for producing test messages to Kafka.
 * This is used to test the consumer with the logging framework.
 */
@Service
public class KafkaProducerService {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Send a test message to Kafka.
     * 
     * @param content The message content
     * @param sender The message sender
     * @return The message that was sent
     */
    public KafkaMessage sendMessage(String content, String sender) {
        String id = UUID.randomUUID().toString();
        KafkaMessage message = new KafkaMessage(id, content, sender, System.currentTimeMillis());
        
        log.info("Sending message to Kafka: {}", message);
        kafkaTemplate.send("test-topic", id, message);
        
        return message;
    }
    
    /**
     * Send a test message that will cause an exception.
     * 
     * @param content The message content
     * @param sender The message sender
     * @return The message that was sent
     */
    public KafkaMessage sendErrorMessage(String content, String sender) {
        String id = "error-" + UUID.randomUUID().toString();
        KafkaMessage message = new KafkaMessage(id, content, sender, System.currentTimeMillis());
        
        log.info("Sending error message to Kafka: {}", message);
        kafkaTemplate.send("test-topic", id, message);
        
        return message;
    }
}
