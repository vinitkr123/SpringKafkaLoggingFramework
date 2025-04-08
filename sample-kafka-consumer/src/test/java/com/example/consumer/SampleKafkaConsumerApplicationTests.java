package com.example.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test for the sample Kafka consumer application.
 * Uses an embedded Kafka broker for testing.
 */
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
public class SampleKafkaConsumerApplicationTests {

    @Test
    public void contextLoads() {
        // Verify that the application context loads successfully
    }
}
