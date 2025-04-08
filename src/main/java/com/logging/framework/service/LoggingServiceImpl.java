package com.logging.framework.service;

import com.logging.framework.model.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Implementation of the LoggingService interface.
 * Provides concrete implementation for logging different types of events.
 */
@Service
public class LoggingServiceImpl implements LoggingService {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingServiceImpl.class);
    
    @Override
    public void logMethodEntry(String className, String methodName, Object[] args) {
        if (log.isDebugEnabled()) {
            log.debug("Entering method [{}#{}] with arguments: {}", 
                    className, methodName, formatArguments(args));
        }
    }
    
    @Override
    public void logMethodExit(String className, String methodName, Object result, long executionTimeMs) {
        if (log.isDebugEnabled()) {
            log.debug("Exiting method [{}#{}] with result: {} (execution time: {} ms)", 
                    className, methodName, formatResult(result), executionTimeMs);
        }
    }
    
    @Override
    public void logKafkaConsumerEvent(LoggingEvent event) {
        if (log.isInfoEnabled()) {
            log.info("Kafka message processed: {} in {}ms by [{}#{}]", 
                    event.getKafkaMessageContext(),
                    event.getExecutionTimeMs(),
                    event.getClassName(),
                    event.getMethodName());
            
            if (log.isDebugEnabled() && event.getKafkaMessageContext() != null) {
                log.debug("Kafka message payload: {}", 
                        formatResult(event.getKafkaMessageContext().getPayload()));
            }
        }
    }
    
    @Override
    public void logException(String className, String methodName, Throwable exception, Object[] args) {
        log.error("Exception in [{}#{}] with arguments: {}", 
                className, methodName, formatArguments(args), exception);
    }
    
    @Override
    public void logEvent(LoggingEvent event) {
        String logLevel = event.getLogLevel() != null ? event.getLogLevel() : "INFO";
        
        switch (logLevel.toUpperCase()) {
            case "DEBUG":
                if (log.isDebugEnabled()) {
                    log.debug("Method [{}#{}] executed in {}ms with result: {}", 
                            event.getClassName(), event.getMethodName(), 
                            event.getExecutionTimeMs(), formatResult(event.getResult()));
                }
                break;
            case "INFO":
                if (log.isInfoEnabled()) {
                    log.info("Method [{}#{}] executed in {}ms", 
                            event.getClassName(), event.getMethodName(), 
                            event.getExecutionTimeMs());
                }
                break;
            case "WARN":
                log.warn("Method [{}#{}] executed in {}ms with result: {}", 
                        event.getClassName(), event.getMethodName(), 
                        event.getExecutionTimeMs(), formatResult(event.getResult()));
                break;
            case "ERROR":
                log.error("Method [{}#{}] executed in {}ms with result: {}", 
                        event.getClassName(), event.getMethodName(), 
                        event.getExecutionTimeMs(), formatResult(event.getResult()));
                break;
            default:
                if (log.isInfoEnabled()) {
                    log.info("Method [{}#{}] executed in {}ms", 
                            event.getClassName(), event.getMethodName(), 
                            event.getExecutionTimeMs());
                }
        }
        
        if (event.getException() != null) {
            log.error("Exception in [{}#{}]", 
                    event.getClassName(), event.getMethodName(), event.getException());
        }
    }
    
    /**
     * Format method arguments for logging.
     * 
     * @param args The method arguments
     * @return Formatted string representation of arguments
     */
    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        return Arrays.toString(args);
    }
    
    /**
     * Format method result for logging.
     * 
     * @param result The method result
     * @return Formatted string representation of result
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }
        return result.toString();
    }
}
