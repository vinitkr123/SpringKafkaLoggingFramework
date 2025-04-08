package com.example.consumer.controller;

import com.example.consumer.model.KafkaMessage;
import com.example.consumer.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for testing the Kafka logging framework.
 * Provides endpoints to send test messages and trigger different scenarios.
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private KafkaProducerService producerService;
    
    /**
     * Send a test message to Kafka.
     * 
     * @param content The message content
     * @return The sent message
     */
    @PostMapping("/send")
    public KafkaMessage sendMessage(@RequestParam String content) {
        KafkaMessage message = new KafkaMessage();
        message.setId(UUID.randomUUID().toString());
        message.setContent(content);
        message.setTimestamp(System.currentTimeMillis());
        
        producerService.sendMessage(message);
        
        return message;
    }
    
    /**
     * Send a test message that will trigger an error.
     * 
     * @param content The message content
     * @return The sent message
     */
    @PostMapping("/send-error")
    public KafkaMessage sendErrorMessage(@RequestParam String content) {
        KafkaMessage message = new KafkaMessage();
        message.setId("error-trigger");
        message.setContent(content);
        message.setTimestamp(System.currentTimeMillis());
        
        producerService.sendMessage(message);
        
        return message;
    }
    
    /**
     * Get test status.
     * 
     * @return Status message
     */
    @GetMapping("/status")
    public String getStatus() {
        return "Kafka Logging Framework Test Controller is running";
    }
}
