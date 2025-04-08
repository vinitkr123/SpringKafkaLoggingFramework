package com.logging.framework.config;

import com.logging.framework.aspect.CustomMethodLoggingAspect;
import com.logging.framework.aspect.KafkaConsumerLoggingAspect;
import com.logging.framework.aspect.PredefinedMethodLoggingAspect;
import com.logging.framework.exception.KafkaExceptionHandler;
import com.logging.framework.exception.LoggingErrorHandler;
import com.logging.framework.service.LoggingService;
import com.logging.framework.service.LoggingServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

/**
 * Auto-configuration for the Kafka logging framework.
 * Automatically configures the beans required for the framework.
 */
@Configuration
@ConditionalOnClass(KafkaListener.class)
@EnableConfigurationProperties(KafkaLoggingProperties.class)
@AutoConfigureAfter(KafkaAutoConfiguration.class)
@ConditionalOnProperty(name = "kafka.logging.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaLoggingAutoConfiguration {
    
    /**
     * Configure the logging service.
     * 
     * @return The logging service
     */
    @Bean
    @ConditionalOnMissingBean
    public LoggingService loggingService() {
        return new LoggingServiceImpl();
    }
    
    /**
     * Configure the Kafka consumer logging aspect.
     * 
     * @param loggingService The logging service
     * @return The Kafka consumer logging aspect
     */
    @Bean
    public KafkaConsumerLoggingAspect kafkaConsumerLoggingAspect(LoggingService loggingService) {
        return new KafkaConsumerLoggingAspect();
    }
    
    /**
     * Configure the predefined method logging aspect.
     * 
     * @param loggingService The logging service
     * @param properties The Kafka logging properties
     * @return The predefined method logging aspect
     */
    @Bean
    public PredefinedMethodLoggingAspect predefinedMethodLoggingAspect(
            LoggingService loggingService, KafkaLoggingProperties properties) {
        return new PredefinedMethodLoggingAspect();
    }
    
    /**
     * Configure the custom method logging aspect.
     * 
     * @param loggingService The logging service
     * @return The custom method logging aspect
     */
    @Bean
    public CustomMethodLoggingAspect customMethodLoggingAspect(LoggingService loggingService) {
        return new CustomMethodLoggingAspect();
    }
    
    /**
     * Configure the Kafka exception handler.
     * 
     * @param loggingService The logging service
     * @return The Kafka exception handler
     */
    @Bean
    public KafkaExceptionHandler kafkaExceptionHandler(LoggingService loggingService) {
        return new KafkaExceptionHandler();
    }
    
    /**
     * Configure the logging error handler.
     * 
     * @param loggingService The logging service
     * @return The logging error handler
     */
    @Bean
    public LoggingErrorHandler loggingErrorHandler(LoggingService loggingService) {
        return new LoggingErrorHandler();
    }
    
    /**
     * Configure the Kafka listener container factory with error handling.
     * This method customizes the existing factory to use our error handler.
     * 
     * @param kafkaListenerContainerFactory The Kafka listener container factory
     * @param loggingErrorHandler The logging error handler
     * @return The customized Kafka listener container factory
     */
    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory,
            LoggingErrorHandler loggingErrorHandler) {
        
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = 
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setErrorHandler(loggingErrorHandler);
        
        return factory;
    }
}
