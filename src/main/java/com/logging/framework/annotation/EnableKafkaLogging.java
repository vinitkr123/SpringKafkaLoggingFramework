package com.logging.framework.annotation;

import com.logging.framework.config.KafkaLoggingAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Main annotation to enable Kafka logging framework in a Spring application.
 * Add this annotation to a configuration class or main application class to
 * enable automatic logging of Kafka consumer operations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(KafkaLoggingAutoConfiguration.class)
public @interface EnableKafkaLogging {
}
