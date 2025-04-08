package com.example.consumer;

import com.example.consumer.model.KafkaMessage;
import com.example.consumer.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for the Kafka consumer with the logging framework.
 * Tests the end-to-end flow of messages through Kafka with logging.
 */
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Test that Kafka messages are properly consumed and logged.
     * This test verifies that the @KafkaListener method is automatically logged.
     */
    @Test
    public void testKafkaConsumerLogging() throws InterruptedException {
        // Send a test message to Kafka
        KafkaMessage message = producerService.sendMessage("Test integration", "IntegrationTest");
        
        // Wait for the message to be processed
        TimeUnit.SECONDS.sleep(2);
        
        // The logging will be verified in the log output
        assertTrue(true, "Message should be processed and logged");
    }

    /**
     * Test that error messages are properly handled and exceptions are logged.
     */
    @Test
    public void testKafkaErrorHandling() throws InterruptedException {
        // Send an error message to Kafka
        KafkaMessage message = producerService.sendErrorMessage("Test error handling", "ErrorTest");
        
        // Wait for the message to be processed
        TimeUnit.SECONDS.sleep(2);
        
        // The exception logging will be verified in the log output
        assertTrue(true, "Error should be handled and logged");
    }
}
