package com.logging.framework.config;

import com.logging.framework.appender.KafkaLoggingFileAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the dedicated log file.
 * Sets up file appenders and loggers for the Kafka logging framework.
 */
@Configuration
@EnableConfigurationProperties(KafkaLoggingProperties.class)
@ConditionalOnProperty(name = "kafka.logging.log-file.enabled", havingValue = "true", matchIfMissing = true)
public class LogFileConfiguration {
    
    private final KafkaLoggingProperties properties;
    
    @Autowired
    public LogFileConfiguration(KafkaLoggingProperties properties) {
        this.properties = properties;
    }
    
    /**
     * Create the Kafka logging file appender.
     * 
     * @return The Kafka logging file appender
     */
    @Bean
    public KafkaLoggingFileAppender kafkaLoggingFileAppender() {
        return new KafkaLoggingFileAppender(properties);
    }
}
