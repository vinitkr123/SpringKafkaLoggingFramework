package com.example.consumer.config;

import com.logging.framework.annotation.EnableKafkaLogging;
import com.logging.framework.annotation.LogMethodPattern;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the sample Kafka consumer application.
 * Enables the Kafka logging framework with enhanced features.
 */
@Configuration
@EnableKafkaLogging(
    enableMethodSelection = true,
    enableLogFile = true,
    enableStatusTracking = true
)
@LogMethodPattern(
    value = {"process*", "handle*", "consume*"},
    exclude = {"getConsumerConfig", "getProducerConfig"}
)
public class LoggingConfig {
    // Configuration is done through annotations and properties
}
