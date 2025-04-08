package com.logging.framework.config;

import com.logging.framework.annotation.EnableKafkaLogging;
import com.logging.framework.annotation.LogMethodPattern;
import com.logging.framework.aspect.CustomMethodLoggingAspect;
import com.logging.framework.aspect.KafkaConsumerLoggingAspect;
import com.logging.framework.aspect.PredefinedMethodLoggingAspect;
import com.logging.framework.exception.KafkaExceptionHandler;
import com.logging.framework.service.LoggingService;
import com.logging.framework.service.LoggingServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

/**
 * Auto-configuration for the Kafka logging framework.
 * This class sets up the necessary beans for the framework to work.
 */
@Configuration
@EnableConfigurationProperties(KafkaLoggingProperties.class)
@ConditionalOnBean(annotation = EnableKafkaLogging.class)
public class KafkaLoggingAutoConfiguration {
    
    @Autowired
    private KafkaLoggingProperties properties;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * Create the logging service bean.
     * 
     * @return The logging service
     */
    @Bean
    @ConditionalOnMissingBean
    public LoggingService loggingService() {
        return new LoggingServiceImpl();
    }
    
    /**
     * Create the Kafka consumer logging aspect bean.
     * 
     * @return The Kafka consumer logging aspect
     */
    @Bean
    @ConditionalOnProperty(name = "kafka.logging.enabled", havingValue = "true", matchIfMissing = true)
    public KafkaConsumerLoggingAspect kafkaConsumerLoggingAspect() {
        return new KafkaConsumerLoggingAspect();
    }
    
    /**
     * Create the custom method logging aspect bean.
     * 
     * @return The custom method logging aspect
     */
    @Bean
    @ConditionalOnProperty(name = "kafka.logging.enabled", havingValue = "true", matchIfMissing = true)
    public CustomMethodLoggingAspect customMethodLoggingAspect() {
        return new CustomMethodLoggingAspect();
    }
    
    /**
     * Create the predefined method logging aspect bean.
     * 
     * @return The predefined method logging aspect
     */
    @Bean
    @ConditionalOnProperty(name = "kafka.logging.enabled", havingValue = "true", matchIfMissing = true)
    public PredefinedMethodLoggingAspect predefinedMethodLoggingAspect() {
        PredefinedMethodLoggingAspect aspect = new PredefinedMethodLoggingAspect();
        
        // Process @LogMethodPattern annotations
        processLogMethodPatternAnnotations();
        
        return aspect;
    }
    
    /**
     * Create the Kafka exception handler bean.
     * 
     * @return The Kafka exception handler
     */
    @Bean
    @ConditionalOnProperty(name = "kafka.logging.enabled", havingValue = "true", matchIfMissing = true)
    public KafkaExceptionHandler kafkaExceptionHandler() {
        return new KafkaExceptionHandler();
    }
    
    /**
     * Process @LogMethodPattern annotations and add them to the configuration.
     */
    private void processLogMethodPatternAnnotations() {
        // Get all beans with @LogMethodPattern annotation
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(LogMethodPattern.class);
        
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();
            LogMethodPattern annotation = AnnotationUtils.findAnnotation(beanClass, LogMethodPattern.class);
            
            if (annotation != null) {
                // Add include patterns
                for (String pattern : annotation.value()) {
                    String fullPattern = beanClass.getName() + "." + pattern;
                    properties.getMethodSelection().getIncludePatterns().add(fullPattern);
                }
                
                // Add exclude patterns
                for (String pattern : annotation.exclude()) {
                    String fullPattern = beanClass.getName() + "." + pattern;
                    properties.getMethodSelection().getExcludePatterns().add(fullPattern);
                }
            }
        }
    }
}
