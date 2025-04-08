package com.logging.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for the Kafka logging framework.
 * These properties can be set in application.properties or application.yml.
 */
@ConfigurationProperties(prefix = "kafka.logging")
public class KafkaLoggingProperties {
    
    /**
     * Whether the Kafka logging framework is enabled.
     * Default is true.
     */
    private boolean enabled = true;
    
    /**
     * List of predefined method patterns to log.
     * Patterns can include wildcards (*).
     * Example: com.example.service.*Service.process*
     */
    private List<String> predefinedMethods = new ArrayList<>();
    
    /**
     * Log level for logging events.
     * Default is INFO.
     */
    private String logLevel = "INFO";
    
    /**
     * Whether to include message payloads in logs.
     * Default is true.
     */
    private boolean includePayload = true;
    
    /**
     * Whether to mask sensitive data in logs.
     * Default is true.
     */
    private boolean maskSensitiveData = true;
    
    /**
     * Whether to use asynchronous logging.
     * Default is true.
     */
    private boolean asyncLogging = true;
    
    /**
     * List of sensitive field names to mask in logs.
     */
    private List<String> sensitiveFields = new ArrayList<>();
    
    /**
     * Character to use for masking sensitive data.
     * Default is '*'.
     */
    private char maskingChar = '*';
    
    // Getters and Setters
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public List<String> getPredefinedMethods() {
        return predefinedMethods;
    }
    
    public void setPredefinedMethods(List<String> predefinedMethods) {
        this.predefinedMethods = predefinedMethods;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    public boolean isIncludePayload() {
        return includePayload;
    }
    
    public void setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
    }
    
    public boolean isMaskSensitiveData() {
        return maskSensitiveData;
    }
    
    public void setMaskSensitiveData(boolean maskSensitiveData) {
        this.maskSensitiveData = maskSensitiveData;
    }
    
    public boolean isAsyncLogging() {
        return asyncLogging;
    }
    
    public void setAsyncLogging(boolean asyncLogging) {
        this.asyncLogging = asyncLogging;
    }
    
    public List<String> getSensitiveFields() {
        return sensitiveFields;
    }
    
    public void setSensitiveFields(List<String> sensitiveFields) {
        this.sensitiveFields = sensitiveFields;
    }
    
    public char getMaskingChar() {
        return maskingChar;
    }
    
    public void setMaskingChar(char maskingChar) {
        this.maskingChar = maskingChar;
    }
}
