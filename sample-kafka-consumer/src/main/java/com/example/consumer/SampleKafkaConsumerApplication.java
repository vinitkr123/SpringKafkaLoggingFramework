package com.example.consumer;

import com.logging.framework.annotation.EnableKafkaLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the sample Kafka consumer.
 * Uses @EnableKafkaLogging to enable the logging framework.
 */
@EnableKafkaLogging
@SpringBootApplication
public class SampleKafkaConsumerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SampleKafkaConsumerApplication.class, args);
    }
}
