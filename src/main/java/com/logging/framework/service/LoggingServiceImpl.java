package com.logging.framework.service;

import com.logging.framework.appender.KafkaLoggingFileAppender;
import com.logging.framework.model.LoggingEvent;
import com.logging.framework.model.MethodExecutionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Implementation of the LoggingService interface.
 * Provides concrete implementation for logging different types of events.
 */
@Service
public class LoggingServiceImpl implements LoggingService {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingServiceImpl.class);
    
    @Autowired(required = false)
    private KafkaLoggingFileAppender fileAppender;
    
    @Override
    public void logMethodEntry(String className, String methodName, Object[] args) {
        if (log.isDebugEnabled()) {
            log.debug("Entering method [{}#{}] with arguments: {}", 
                    className, methodName, formatArguments(args));
        }
        
        // Log to dedicated file if appender is available
        if (fileAppender != null) {
            LoggingEvent event = new LoggingEvent();
            event.setClassName(className);
            event.setMethodName(methodName);
            event.setArguments(args);
            event.setStatus(MethodExecutionStatus.IN_PROGRESS);
            event.setLogLevel("DEBUG");
            event.addContext("action", "method_entry");
            
            fileAppender.log(event);
        }
    }
    
    @Override
    public void logMethodExit(String className, String methodName, Object result, long executionTimeMs, MethodExecutionStatus status) {
        if (log.isDebugEnabled()) {
            log.debug("Exiting method [{}#{}] with result: {} (execution time: {} ms, status: {})", 
                    className, methodName, formatResult(result), executionTimeMs, status);
        }
        
        // Log to dedicated file if appender is available
        if (fileAppender != null) {
            LoggingEvent event = new LoggingEvent();
            event.setClassName(className);
            event.setMethodName(methodName);
            event.setResult(result);
            event.setExecutionTimeMs(executionTimeMs);
            event.setStatus(status);
            event.setLogLevel("INFO");
            event.addContext("action", "method_exit");
            
            fileAppender.log(event);
        }
    }
    
    @Override
    public void logMethodExit(String className, String methodName, Object result, long executionTimeMs) {
        logMethodExit(className, methodName, result, executionTimeMs, MethodExecutionStatus.PASSED);
    }
    
    @Override
    public void logKafkaConsumerEvent(LoggingEvent event) {
        if (log.isInfoEnabled()) {
            log.info("Kafka message processed: {} in {}ms by [{}#{}] - Status: {}", 
                    event.getKafkaMessageContext(),
                    event.getExecutionTimeMs(),
                    event.getClassName(),
                    event.getMethodName(),
                    event.getStatus());
            
            if (log.isDebugEnabled() && event.getKafkaMessageContext() != null) {
                log.debug("Kafka message payload: {}", 
                        formatResult(event.getKafkaMessageContext().getPayload()));
            }
        }
        
        // Log to dedicated file if appender is available
        if (fileAppender != null) {
            event.addContext("action", "kafka_consumer");
            fileAppender.log(event);
        }
    }
    
    @Override
    public void logException(String className, String methodName, Throwable exception, Object[] args) {
        log.error("Exception in [{}#{}] with arguments: {}", 
                className, methodName, formatArguments(args), exception);
        
        // Log to dedicated file if appender is available
        if (fileAppender != null) {
            LoggingEvent event = new LoggingEvent();
            event.setClassName(className);
            event.setMethodName(methodName);
            event.setArguments(args);
            event.setException(exception);
            event.setStatus(MethodExecutionStatus.FAILED);
            event.setLogLevel("ERROR");
            event.addContext("action", "exception");
            
            fileAppender.log(event);
        }
    }
    
    @Override
    public void logEvent(LoggingEvent event) {
        String logLevel = event.getLogLevel() != null ? event.getLogLevel() : "INFO";
        
        switch (logLevel.toUpperCase()) {
            case "DEBUG":
                if (log.isDebugEnabled()) {
                    log.debug("Method [{}#{}] executed in {}ms with result: {} - Status: {}", 
                            event.getClassName(), event.getMethodName(), 
                            event.getExecutionTimeMs(), formatResult(event.getResult()),
                            event.getStatus());
                }
                break;
            case "INFO":
                if (log.isInfoEnabled()) {
                    log.info("Method [{}#{}] executed in {}ms - Status: {}", 
                            event.getClassName(), event.getMethodName(), 
                            event.getExecutionTimeMs(), event.getStatus());
                }
                break;
            case "WARN":
                log.warn("Method [{}#{}] executed in {}ms with result: {} - Status: {}", 
                        event.getClassName(), event.getMethodName(), 
                        event.getExecutionTimeMs(), formatResult(event.getResult()),
                        event.getStatus());
                break;
            case "ERROR":
                log.error("Method [{}#{}] executed in {}ms with result: {} - Status: {}", 
                        event.getClassName(), event.getMethodName(), 
                        event.getExecutionTimeMs(), formatResult(event.getResult()),
                        event.getStatus());
                break;
            default:
                if (log.isInfoEnabled()) {
                    log.info("Method [{}#{}] executed in {}ms - Status: {}", 
                            event.getClassName(), event.getMethodName(), 
                            event.getExecutionTimeMs(), event.getStatus());
                }
        }
        
        if (event.getException() != null) {
            log.error("Exception in [{}#{}]", 
                    event.getClassName(), event.getMethodName(), event.getException());
        }
        
        // Log to dedicated file if appender is available
        if (fileAppender != null) {
            event.addContext("action", "complete_event");
            fileAppender.log(event);
        }
    }
    
    @Override
    public void logMethodStatus(String className, String methodName, MethodExecutionStatus status, String message) {
        if (log.isInfoEnabled()) {
            log.info("Method [{}#{}] status: {} - {}", 
                    className, methodName, status, message);
        }
        
        // Log to dedicated file if appender is available
        if (fileAppender != null) {
            LoggingEvent event = new LoggingEvent();
            event.setClassName(className);
            event.setMethodName(methodName);
            event.setStatus(status);
            event.setLogLevel("INFO");
            event.addContext("action", "status_update");
            event.addContext("message", message);
            
            fileAppender.log(event);
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
