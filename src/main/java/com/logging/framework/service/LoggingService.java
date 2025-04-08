package com.logging.framework.service;

import com.logging.framework.model.LoggingEvent;
import com.logging.framework.model.MethodExecutionStatus;

/**
 * Interface for logging services.
 * Provides methods for logging different types of events.
 */
public interface LoggingService {
    
    /**
     * Log a method entry event.
     * 
     * @param className The class name
     * @param methodName The method name
     * @param args The method arguments
     */
    void logMethodEntry(String className, String methodName, Object[] args);
    
    /**
     * Log a method exit event.
     * 
     * @param className The class name
     * @param methodName The method name
     * @param result The method result
     * @param executionTimeMs The method execution time in milliseconds
     * @param status The method execution status
     */
    void logMethodExit(String className, String methodName, Object result, long executionTimeMs, MethodExecutionStatus status);
    
    /**
     * Log a method exit event.
     * 
     * @param className The class name
     * @param methodName The method name
     * @param result The method result
     * @param executionTimeMs The method execution time in milliseconds
     */
    void logMethodExit(String className, String methodName, Object result, long executionTimeMs);
    
    /**
     * Log a Kafka consumer event.
     * 
     * @param event The logging event containing Kafka message details
     */
    void logKafkaConsumerEvent(LoggingEvent event);
    
    /**
     * Log an exception event.
     * 
     * @param className The class name
     * @param methodName The method name
     * @param exception The exception that occurred
     * @param args The method arguments
     */
    void logException(String className, String methodName, Throwable exception, Object[] args);
    
    /**
     * Log a complete event.
     * 
     * @param event The logging event
     */
    void logEvent(LoggingEvent event);
    
    /**
     * Log a method status event.
     * 
     * @param className The class name
     * @param methodName The method name
     * @param status The method execution status
     * @param message Additional message
     */
    void logMethodStatus(String className, String methodName, MethodExecutionStatus status, String message);
}
