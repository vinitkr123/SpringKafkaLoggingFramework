package com.logging.framework.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Model representing a logging event.
 * Contains details about the method execution, arguments, result, timing, and status.
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
    private MethodExecutionStatus status;
    private Map<String, Object> additionalContext;
    
    public LoggingEvent() {
        this.timestamp = LocalDateTime.now();
        this.status = MethodExecutionStatus.IN_PROGRESS;
        this.additionalContext = new HashMap<>();
    }
    
    /**
     * Add additional context information to the logging event.
     * 
     * @param key The context key
     * @param value The context value
     * @return This logging event for method chaining
     */
    public LoggingEvent addContext(String key, Object value) {
        this.additionalContext.put(key, value);
        return this;
    }
    
    /**
     * Get a formatted string representation of the logging event for the log file.
     * 
     * @return Formatted log entry
     */
    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        
        // Add class and method
        sb.append("[").append(className).append("#").append(methodName).append("] ");
        
        // Add status
        sb.append("[").append(status).append("] ");
        
        // Add message based on status
        if (status == MethodExecutionStatus.PASSED) {
            sb.append("Method executed successfully");
        } else if (status == MethodExecutionStatus.FAILED) {
            sb.append("Method execution failed");
        } else {
            sb.append("Method execution in progress");
        }
        
        // Add execution time if available
        if (executionTimeMs > 0) {
            sb.append(" | Duration: ").append(executionTimeMs).append(" ms");
        }
        
        // Add Kafka context if available
        if (kafkaMessageContext != null) {
            sb.append(" | Kafka: ").append(kafkaMessageContext);
        }
        
        // Add additional context if available
        if (!additionalContext.isEmpty()) {
            sb.append(" | Context: ").append(additionalContext);
        }
        
        // Add exception if available
        if (exception != null) {
            sb.append(" | Exception: ").append(exception.getClass().getSimpleName())
              .append(": ").append(exception.getMessage());
        }
        
        return sb.toString();
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
        if (exception != null) {
            this.status = MethodExecutionStatus.FAILED;
        }
    }
    
    public KafkaMessageContext getKafkaMessageContext() {
        return kafkaMessageContext;
    }
    
    public void setKafkaMessageContext(KafkaMessageContext kafkaMessageContext) {
        this.kafkaMessageContext = kafkaMessageContext;
    }
    
    public MethodExecutionStatus getStatus() {
        return status;
    }
    
    public void setStatus(MethodExecutionStatus status) {
        this.status = status;
    }
    
    public Map<String, Object> getAdditionalContext() {
        return additionalContext;
    }
    
    public void setAdditionalContext(Map<String, Object> additionalContext) {
        this.additionalContext = additionalContext;
    }
}
