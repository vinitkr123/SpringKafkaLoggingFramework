package com.logging.framework.annotation;

import org.springframework.context.annotation.Import;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.logging.framework.config.KafkaLoggingAutoConfiguration;
import com.logging.framework.config.LogFileConfiguration;

/**
 * Annotation to enable Kafka logging framework.
 * Add this annotation to a Spring Boot application class to enable the Kafka logging framework.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({KafkaLoggingAutoConfiguration.class, LogFileConfiguration.class})
public @interface EnableKafkaLogging {
    
    /**
     * Whether to enable method selection configuration.
     * Default is true.
     * 
     * @return true if method selection configuration is enabled, false otherwise
     */
    boolean enableMethodSelection() default true;
    
    /**
     * Whether to enable dedicated log file.
     * Default is true.
     * 
     * @return true if dedicated log file is enabled, false otherwise
     */
    boolean enableLogFile() default true;
    
    /**
     * Whether to enable status tracking.
     * Default is true.
     * 
     * @return true if status tracking is enabled, false otherwise
     */
    boolean enableStatusTracking() default true;
}
