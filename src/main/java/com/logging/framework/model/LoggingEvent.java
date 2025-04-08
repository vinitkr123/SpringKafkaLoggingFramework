package com.logging.framework.model;

import java.time.LocalDateTime;

/**
 * Model representing a logging event.
 * Contains details about the method execution, arguments, result, and timing.
 */
public class LoggingEvent {
    
    private String methodName;
    private String className;
    private Object[] arguments;
    private Object result;
    private long executionTimeMs;
    private LocalDateTime timestamp;
    private String logLevel;
    private Throwable exception;
    private KafkaMessageContext kafkaMessageContext;
    
    public LoggingEvent() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public Object[] getArguments() {
        return arguments;
    }
    
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
    
    public Object getResult() {
        return result;
    }
    
    public void setResult(Object result) {
        this.result = result;
    }
    
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
    
    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    public Throwable getException() {
        return exception;
    }
    
    public void setException(Throwable exception) {
        this.exception = exception;
    }
    
    public KafkaMessageContext getKafkaMessageContext() {
        return kafkaMessageContext;
    }
    
    public void setKafkaMessageContext(KafkaMessageContext kafkaMessageContext) {
        this.kafkaMessageContext = kafkaMessageContext;
    }
}
