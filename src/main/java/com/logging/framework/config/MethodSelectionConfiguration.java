package com.logging.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration properties for method selection in the Kafka logging framework.
 * These properties can be set in application.properties or application.yml.
 */
@ConfigurationProperties(prefix = "kafka.logging.method-selection")
@PropertySource("classpath:method-selection.properties")
public class MethodSelectionConfiguration {
    
    /**
     * Whether to enable method selection.
     * Default is true.
     */
    private boolean enabled = true;
    
    /**
     * Whether to log all methods by default.
     * Default is false.
     */
    private boolean logAllByDefault = false;
    
    /**
     * Whether to log methods that throw exceptions.
     * Default is true.
     */
    private boolean logExceptions = true;
    
    /**
     * Whether to log methods that return specific values.
     * Default is false.
     */
    private boolean logSpecificReturnValues = false;
    
    /**
     * Return values to log.
     * Only used if logSpecificReturnValues is true.
     */
    private String[] returnValuesToLog = {};
    
    /**
     * Method name patterns to include.
     * Supports wildcards (*).
     */
    private String[] includeMethodPatterns = {};
    
    /**
     * Method name patterns to exclude.
     * Supports wildcards (*).
     */
    private String[] excludeMethodPatterns = {};
    
    /**
     * Class name patterns to include.
     * Supports wildcards (*).
     */
    private String[] includeClassPatterns = {};
    
    /**
     * Class name patterns to exclude.
     * Supports wildcards (*).
     */
    private String[] excludeClassPatterns = {};
    
    /**
     * Package name patterns to include.
     * Supports wildcards (*).
     */
    private String[] includePackagePatterns = {};
    
    /**
     * Package name patterns to exclude.
     * Supports wildcards (*).
     */
    private String[] excludePackagePatterns = {};
    
    // Getters and Setters
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isLogAllByDefault() {
        return logAllByDefault;
    }
    
    public void setLogAllByDefault(boolean logAllByDefault) {
        this.logAllByDefault = logAllByDefault;
    }
    
    public boolean isLogExceptions() {
        return logExceptions;
    }
    
    public void setLogExceptions(boolean logExceptions) {
        this.logExceptions = logExceptions;
    }
    
    public boolean isLogSpecificReturnValues() {
        return logSpecificReturnValues;
    }
    
    public void setLogSpecificReturnValues(boolean logSpecificReturnValues) {
        this.logSpecificReturnValues = logSpecificReturnValues;
    }
    
    public String[] getReturnValuesToLog() {
        return returnValuesToLog;
    }
    
    public void setReturnValuesToLog(String[] returnValuesToLog) {
        this.returnValuesToLog = returnValuesToLog;
    }
    
    public String[] getIncludeMethodPatterns() {
        return includeMethodPatterns;
    }
    
    public void setIncludeMethodPatterns(String[] includeMethodPatterns) {
        this.includeMethodPatterns = includeMethodPatterns;
    }
    
    public String[] getExcludeMethodPatterns() {
        return excludeMethodPatterns;
    }
    
    public void setExcludeMethodPatterns(String[] excludeMethodPatterns) {
        this.excludeMethodPatterns = excludeMethodPatterns;
    }
    
    public String[] getIncludeClassPatterns() {
        return includeClassPatterns;
    }
    
    public void setIncludeClassPatterns(String[] includeClassPatterns) {
        this.includeClassPatterns = includeClassPatterns;
    }
    
    public String[] getExcludeClassPatterns() {
        return excludeClassPatterns;
    }
    
    public void setExcludeClassPatterns(String[] excludeClassPatterns) {
        this.excludeClassPatterns = excludeClassPatterns;
    }
    
    public String[] getIncludePackagePatterns() {
        return includePackagePatterns;
    }
    
    public void setIncludePackagePatterns(String[] includePackagePatterns) {
        this.includePackagePatterns = includePackagePatterns;
    }
    
    public String[] getExcludePackagePatterns() {
        return excludePackagePatterns;
    }
    
    public void setExcludePackagePatterns(String[] excludePackagePatterns) {
        this.excludePackagePatterns = excludePackagePatterns;
    }
}
