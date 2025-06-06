package com.logging.framework.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;

import com.logging.framework.config.KafkaLoggingProperties;
import com.logging.framework.model.LoggingEvent;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Custom log appender for writing to the dedicated Kafka logging file.
 * Formats log entries with status information and handles file creation.
 */
public class KafkaLoggingFileAppender {
    
    private final Logger kafkaLogger;
    private final LogFileManager logFileManager;
    private final KafkaLoggingProperties properties;
    
    /**
     * Create a new KafkaLoggingFileAppender.
     * 
     * @param properties The Kafka logging properties
     */
    public KafkaLoggingFileAppender(KafkaLoggingProperties properties) {
        this.properties = properties;
        
        // Create log file manager
        this.logFileManager = new LogFileManager(
                properties.getLogFile().getPath(),
                properties.getLogFile().getFilename());
        
        // Create and configure logger
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.kafkaLogger = loggerContext.getLogger("com.logging.framework.kafka");
        
        // Configure appender
        configureAppender(loggerContext);
    }
    
    /**
     * Configure the file appender for the logger.
     * 
     * @param loggerContext The logger context
     */
    private void configureAppender(LoggerContext loggerContext) {
        // Create encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(properties.getLogFile().getPattern());
        encoder.start();
        
        // Create appender
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(loggerContext);
        appender.setName("KAFKA_FILE");
        appender.setFile(logFileManager.getLogFilePath());
        appender.setEncoder(encoder);
        
        // Configure rolling policy
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setParent(appender);
        rollingPolicy.setFileNamePattern(properties.getLogFile().getPath() + "/" + 
                properties.getLogFile().getFilename() + ".%d{yyyy-MM-dd}.%i.gz");
        rollingPolicy.setMaxHistory(properties.getLogFile().getMaxHistory());
        rollingPolicy.setMaxFileSize(FileSize.valueOf(properties.getLogFile().getMaxSize()));
        rollingPolicy.start();
        
        appender.setRollingPolicy(rollingPolicy);
        appender.start();
        
        // Add appender to logger
        kafkaLogger.addAppender(appender);
        kafkaLogger.setLevel(Level.toLevel(properties.getLogLevel()));
        kafkaLogger.setAdditive(false);
    }
    
    /**
     * Log a message to the dedicated Kafka log file.
     * 
     * @param event The logging event
     */
    public void log(LoggingEvent event) {
        try {
            // Set MDC values for the log pattern
            MDC.put("status", event.getStatus().toString());
            MDC.put("class", event.getClassName());
            MDC.put("method", event.getMethodName());
            
            // Log the message with the appropriate level
            Level level = Level.toLevel(event.getLogLevel(), Level.INFO);
            
            String logMsg = event.toJsonString();
            if (level == Level.ERROR) {
                kafkaLogger.error(logMsg);
            } else if (level == Level.WARN) {
                kafkaLogger.warn(logMsg);
            } else if (level == Level.INFO) {
                kafkaLogger.info(logMsg);
            } else if (level == Level.DEBUG) {
                kafkaLogger.debug(logMsg);
            } else if (level == Level.TRACE) {
                kafkaLogger.trace(logMsg);
            } else {
                kafkaLogger.info(logMsg);
            }
        } finally {
            // Clear MDC values
            MDC.remove("status");
            MDC.remove("class");
            MDC.remove("method");
        }
    }
    
    /**
     * Get the log file manager.
     * 
     * @return The log file manager
     */
    public LogFileManager getLogFileManager() {
        return logFileManager;
    }
}
