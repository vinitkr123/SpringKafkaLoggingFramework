package com.example.consumer.service;

import com.example.consumer.model.KafkaMessage;
import com.logging.framework.annotation.LogMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Sample service that consumes messages from Kafka.
 */
@Service
public class KafkaConsumerService {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    /**
     * Process a message from the 'test-topic' Kafka topic.
     * This method is automatically logged by the framework because it has @KafkaListener.
     * 
     * @param message The message to process
     */
    @KafkaListener(topics = "test-topic", groupId = "sample-consumer-group")
    public void processMessage(KafkaMessage message) {
        log.info("Processing message: {}", message.getId());
        
        // Call other methods that will be logged
        validateMessage(message);
        transformMessage(message);
        saveMessage(message);
    }
    
    /**
     * Validate a message.
     * This method will be logged if it matches a predefined pattern in configuration.
     * 
     * @param message The message to validate
     * @return True if the message is valid, false otherwise
     */
    public boolean validateMessage(KafkaMessage message) {
        log.info("Validating message: {}", message.getId());
        
        if (message.getContent() == null || message.getContent().isEmpty()) {
            log.warn("Message content is empty: {}", message.getId());
            return false;
        }
        
        return true;
    }
    
    /**
     * Transform a message.
     * This method is explicitly marked for logging with @LogMethod.
     * 
     * @param message The message to transform
     * @return The transformed message
     */
    @LogMethod(level = "DEBUG", description = "Transform Kafka message")
    public KafkaMessage transformMessage(KafkaMessage message) {
        log.info("Transforming message: {}", message.getId());
        
        // Transform the message (e.g., convert to uppercase)
        message.setContent(message.getContent().toUpperCase());
        
        return message;
    }
    
    /**
     * Save a message.
     * This method will throw an exception to demonstrate exception logging.
     * 
     * @param message The message to save
     */
    public void saveMessage(KafkaMessage message) {
        log.info("Saving message: {}", message.getId());
        
        // Simulate an exception for testing exception handling
        if (message.getId() != null && message.getId().startsWith("error")) {
            throw new RuntimeException("Error saving message: " + message.getId());
        }
        
        // Save the message (simulated)
        log.info("Message saved successfully: {}", message.getId());
    }
}
