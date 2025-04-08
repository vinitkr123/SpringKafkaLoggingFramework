package com.example.consumer.service;

import com.example.consumer.model.KafkaMessage;
import com.logging.framework.annotation.LogMethod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KafkaConsumerService.
 * Tests the logging framework's functionality with the consumer service.
 */
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
public class KafkaConsumerServiceTest {

    @Autowired
    private KafkaConsumerService consumerService;
    
    @Autowired
    private KafkaProducerService producerService;
    
    /**
     * Test that the validateMessage method is logged when it matches
     * the predefined pattern in application.properties.
     */
    @Test
    public void testPredefinedMethodLogging() {
        KafkaMessage message = new KafkaMessage("test-id", "Test content", "TestSender", System.currentTimeMillis());
        boolean result = consumerService.validateMessage(message);
        assertTrue(result, "Message validation should succeed");
        // The logging will be verified in the log output
    }
    
    /**
     * Test that the transformMessage method is logged because it has
     * the @LogMethod annotation.
     */
    @Test
    public void testCustomMethodLogging() {
        KafkaMessage message = new KafkaMessage("test-id", "Test content", "TestSender", System.currentTimeMillis());
        KafkaMessage transformed = consumerService.transformMessage(message);
        assertEquals("TEST CONTENT", transformed.getContent(), "Content should be uppercase");
        // The logging will be verified in the log output
    }
    
    /**
     * Test that exceptions are properly logged by the framework.
     */
    @Test
    public void testExceptionLogging() {
        KafkaMessage message = new KafkaMessage("error-test", "Error content", "ErrorSender", System.currentTimeMillis());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            consumerService.saveMessage(message);
        });
        assertTrue(exception.getMessage().contains("error-test"), "Exception message should contain the message ID");
        // The exception logging will be verified in the log output
    }
}
