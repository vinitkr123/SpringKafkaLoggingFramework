package com.example.consumer;

import com.example.consumer.model.KafkaMessage;
import com.example.consumer.service.KafkaConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the method selection configuration.
 * Tests that the correct methods are logged based on configuration.
 */
@SpringBootTest
@ActiveProfiles("test")
public class MethodSelectionConfigurationTest {

    @Autowired
    private KafkaConsumerService consumerService;

    /**
     * Test that methods matching the configured patterns are logged.
     */
    @Test
    public void testMethodSelectionConfiguration() throws Exception {
        // Create test message
        KafkaMessage message = new KafkaMessage();
        message.setId(UUID.randomUUID().toString());
        message.setContent("Test message for method selection");
        message.setTimestamp(System.currentTimeMillis());

        // Call methods that should be logged based on configuration
        consumerService.processMessage(message);
        consumerService.validateMessage(message);
        consumerService.transformMessage(message);
        
        // Wait for logs to be written
        Thread.sleep(500);
        
        // Check log file exists
        File logDir = new File("./logs");
        assertTrue(logDir.exists(), "Log directory should exist");

        File logFile = new File(logDir, "kafka-consumer.log");
        assertTrue(logFile.exists(), "Log file should exist");
        
        // Read log file content
        List<String> logLines = Files.readAllLines(Paths.get(logFile.getAbsolutePath()));
        
        // Verify that methods matching patterns are logged
        boolean processMethodLogged = logLines.stream()
                .anyMatch(line -> line.contains("processMessage") && line.contains("PASSED"));
        boolean validateMethodLogged = logLines.stream()
                .anyMatch(line -> line.contains("validateMessage") && line.contains("PASSED"));
        boolean transformMethodLogged = logLines.stream()
                .anyMatch(line -> line.contains("transformMessage") && line.contains("PASSED"));
        
        assertTrue(processMethodLogged, "processMessage method should be logged");
        assertTrue(validateMethodLogged, "validateMessage method should be logged");
        assertTrue(transformMethodLogged, "transformMessage method should be logged");
        
        // Call a method that should NOT be logged based on configuration
        consumerService.toString();
        
        // Verify that methods not matching patterns are not logged
        boolean toStringMethodLogged = logLines.stream()
                .anyMatch(line -> line.contains("toString"));
        
        assertFalse(toStringMethodLogged, "toString method should not be logged");
    }
}
