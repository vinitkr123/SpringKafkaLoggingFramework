package com.logging.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Spring Kafka Logging Framework.
 * This class is used when running the framework as a standalone application.
 */
@SpringBootApplication
public class SpringKafkaLoggingFrameworkApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaLoggingFrameworkApplication.class, args);
    }
}
