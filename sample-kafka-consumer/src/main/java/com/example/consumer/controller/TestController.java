package com.example.consumer.controller;

import com.example.consumer.model.KafkaMessage;
import com.example.consumer.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for testing the Kafka logging framework.
 * Provides endpoints to send test messages to Kafka.
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private KafkaProducerService producerService;
    
    /**
     * Send a normal test message to Kafka.
     * 
     * @param content The message content
     * @param sender The message sender
     * @return The message that was sent
     */
    @GetMapping("/send")
    public KafkaMessage sendMessage(
            @RequestParam(defaultValue = "Test message") String content,
            @RequestParam(defaultValue = "TestSender") String sender) {
        return producerService.sendMessage(content, sender);
    }
    
    /**
     * Send a test message that will cause an exception.
     * 
     * @param content The message content
     * @param sender The message sender
     * @return The message that was sent
     */
    @GetMapping("/send-error")
    public KafkaMessage sendErrorMessage(
            @RequestParam(defaultValue = "Error message") String content,
            @RequestParam(defaultValue = "ErrorSender") String sender) {
        return producerService.sendErrorMessage(content, sender);
    }
}
