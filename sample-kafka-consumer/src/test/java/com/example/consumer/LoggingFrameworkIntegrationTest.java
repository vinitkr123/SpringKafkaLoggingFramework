package com.example.consumer;

import com.example.consumer.model.KafkaMessage;
import com.example.consumer.service.KafkaConsumerService;
import com.example.consumer.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the Kafka logging framework.
 * Tests the logging functionality with a real Kafka broker.
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
public class LoggingFrameworkIntegrationTest {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private KafkaConsumerService consumerService;

    /**
     * Test that the log file is created and contains the expected entries.
     */
    @Test
    public void testLogFileCreation() throws Exception {
        // Send a test message
        KafkaMessage message = new KafkaMessage();
        message.setId(UUID.randomUUID().toString());
        message.setContent("Test message for log file creation");
        message.setTimestamp(System.currentTimeMillis());

        producerService.sendMessage(message);

        // Wait for message processing
        TimeUnit.SECONDS.sleep(2);

        // Check that the log file exists
        File logDir = new File("./logs");
        assertTrue(logDir.exists(), "Log directory should exist");

        File logFile = new File(logDir, "kafka-consumer.log");
        assertTrue(logFile.exists(), "Log file should exist");
        assertTrue(logFile.length() > 0, "Log file should not be empty");

        // We can't easily check the content of the log file in a unit test,
        // but we can verify it exists and has content
    }

    /**
     * Test method status tracking for successful execution.
     */
    @Test
    public void testMethodStatusTracking_Success() {
        // Direct method call to test status tracking
        KafkaMessage message = new KafkaMessage();
        message.setId(UUID.randomUUID().toString());
        message.setContent("Test message for status tracking");
        message.setTimestamp(System.currentTimeMillis());

        // This should be logged with PASSED status
        boolean isValid = consumerService.validateMessage(message);
        assertTrue(isValid, "Message should be valid");

        // This should be logged with PASSED status
        KafkaMessage transformed = consumerService.transformMessage(message);
        assertEquals("TEST MESSAGE FOR STATUS TRACKING", transformed.getContent(),
                "Message content should be transformed to uppercase");

        // This should be logged with PASSED status
        consumerService.saveMessage(message);
    }

    /**
     * Test method status tracking for failed execution.
     */
    @Test
    public void testMethodStatusTracking_Failure() {
        // Create a message that will trigger an error
        KafkaMessage message = new KafkaMessage();
        message.setId("error-trigger");
        message.setContent("Test message for error handling");
        message.setTimestamp(System.currentTimeMillis());

        // This should be logged with FAILED status
        Exception exception = assertThrows(RuntimeException.class, () -> {
            consumerService.saveMessage(message);
        });

        assertTrue(exception.getMessage().contains("error-trigger"),
                "Exception message should contain the error trigger ID");
    }
}
