package com.logging.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark Kafka consumer methods for logging.
 * This annotation can be used on methods that process Kafka messages
 * to enable detailed logging of the message processing.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogKafkaConsumer {
    
    /**
     * Log level for the consumer method.
     * Default is INFO.
     */
    String level() default "INFO";
    
    /**
     * Whether to include the message payload in the logs.
     * Default is true.
     */
    boolean includePayload() default true;
    
    /**
     * Whether to include the message headers in the logs.
     * Default is true.
     */
    boolean includeHeaders() default true;
    
    /**
     * Whether to log the processing time.
     * Default is true.
     */
    boolean logProcessingTime() default true;
}
