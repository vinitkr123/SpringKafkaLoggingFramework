package com.example.consumer.service;

import com.example.consumer.model.KafkaMessage;
import com.logging.framework.annotation.LogMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service for consuming Kafka messages.
 */
@Service
public class KafkaConsumerService {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    /**
     * Consume a message from the test topic.
     * This method will be automatically logged by the KafkaConsumerLoggingAspect.
     * 
     * @param message The message to consume
     */
    @KafkaListener(topics = "test-topic", groupId = "sample-consumer-group")
    public void consumeMessage(KafkaMessage message) {
        log.info("Received message: {}", message);
        processMessage(message);
    }
    
    /**
     * Process a Kafka message.
     * This method will be logged because it matches the predefined pattern "process*".
     * 
     * @param message The message to process
     */
    public void processMessage(KafkaMessage message) {
        log.info("Processing message: {}", message);
        // Simulate processing
        try {
            Thread.sleep(100);
            
            // Call additional methods to demonstrate different logging scenarios
            validateMessage(message);
            transformMessage(message);
            saveMessage(message);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Validate a Kafka message.
     * This method will be logged because it's explicitly annotated with @LogMethod.
     * 
     * @param message The message to validate
     * @return true if the message is valid, false otherwise
     */
    @LogMethod(includeArgs = true, includeResult = true, description = "Validating Kafka message")
    public boolean validateMessage(KafkaMessage message) {
        log.debug("Validating message: {}", message);
        // Simple validation logic
        return message != null && message.getContent() != null && !message.getContent().isEmpty();
    }
    
    /**
     * Transform a Kafka message.
     * This method will be logged because it matches a pattern in the configuration.
     * 
     * @param message The message to transform
     * @return The transformed message
     */
    public KafkaMessage transformMessage(KafkaMessage message) {
        log.debug("Transforming message: {}", message);
        // Simple transformation logic
        if (message != null) {
            message.setContent(message.getContent().toUpperCase());
        }
        return message;
    }
    
    /**
     * Save a Kafka message.
     * This method will be logged and will demonstrate error handling and status tracking.
     * 
     * @param message The message to save
     * @throws RuntimeException if the message ID is "error-trigger"
     */
    public void saveMessage(KafkaMessage message) {
        log.debug("Saving message: {}", message);
        
        // Simulate an error condition for testing
        if (message != null && "error-trigger".equals(message.getId())) {
            throw new RuntimeException("Error saving message with ID: " + message.getId());
        }
        
        // Simulate saving to database
        log.info("Message saved successfully: {}", message);
    }
}
