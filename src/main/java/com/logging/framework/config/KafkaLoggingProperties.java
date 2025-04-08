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
    
    /**
     * Configuration for the dedicated log file.
     */
    private LogFileConfig logFile = new LogFileConfig();
    
    /**
     * Configuration for method selection.
     */
    private MethodSelectionConfig methodSelection = new MethodSelectionConfig();
    
    /**
     * Inner class for log file configuration.
     */
    public static class LogFileConfig {
        /**
         * Whether to use a dedicated log file.
         * Default is true.
         */
        private boolean enabled = true;
        
        /**
         * Path to the log file directory.
         * Default is the current directory.
         */
        private String path = "./logs";
        
        /**
         * Name of the log file.
         * Default is kafka-logging.log.
         */
        private String filename = "kafka-logging.log";
        
        /**
         * Maximum size of the log file before rotation.
         * Default is 10MB.
         */
        private String maxSize = "10MB";
        
        /**
         * Maximum number of log files to keep.
         * Default is 7.
         */
        private int maxHistory = 7;
        
        /**
         * Log pattern for the file.
         * Default is a pattern that includes timestamp, level, status, class, method, and message.
         */
        private String pattern = "[%d{yyyy-MM-dd HH:mm:ss}] [%p] [%X{status}] [%X{class}#%X{method}] - %m%n";
        
        // Getters and Setters
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getPath() {
            return path;
        }
        
        public void setPath(String path) {
            this.path = path;
        }
        
        public String getFilename() {
            return filename;
        }
        
        public void setFilename(String filename) {
            this.filename = filename;
        }
        
        public String getMaxSize() {
            return maxSize;
        }
        
        public void setMaxSize(String maxSize) {
            this.maxSize = maxSize;
        }
        
        public int getMaxHistory() {
            return maxHistory;
        }
        
        public void setMaxHistory(int maxHistory) {
            this.maxHistory = maxHistory;
        }
        
        public String getPattern() {
            return pattern;
        }
        
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }
    
    /**
     * Inner class for method selection configuration.
     */
    public static class MethodSelectionConfig {
        /**
         * List of method patterns to include in logging.
         */
        private List<String> includePatterns = new ArrayList<>();
        
        /**
         * List of method patterns to exclude from logging.
         */
        private List<String> excludePatterns = new ArrayList<>();
        
        /**
         * List of packages to include in logging.
         */
        private List<String> includePackages = new ArrayList<>();
        
        /**
         * List of classes to include in logging.
         */
        private List<String> includeClasses = new ArrayList<>();
        
        // Getters and Setters
        
        public List<String> getIncludePatterns() {
            return includePatterns;
        }
        
        public void setIncludePatterns(List<String> includePatterns) {
            this.includePatterns = includePatterns;
        }
        
        public List<String> getExcludePatterns() {
            return excludePatterns;
        }
        
        public void setExcludePatterns(List<String> excludePatterns) {
            this.excludePatterns = excludePatterns;
        }
        
        public List<String> getIncludePackages() {
            return includePackages;
        }
        
        public void setIncludePackages(List<String> includePackages) {
            this.includePackages = includePackages;
        }
        
        public List<String> getIncludeClasses() {
            return includeClasses;
        }
        
        public void setIncludeClasses(List<String> includeClasses) {
            this.includeClasses = includeClasses;
        }
    }
    
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
    
    public LogFileConfig getLogFile() {
        return logFile;
    }
    
    public void setLogFile(LogFileConfig logFile) {
        this.logFile = logFile;
    }
    
    public MethodSelectionConfig getMethodSelection() {
        return methodSelection;
    }
    
    public void setMethodSelection(MethodSelectionConfig methodSelection) {
        this.methodSelection = methodSelection;
    }
}
