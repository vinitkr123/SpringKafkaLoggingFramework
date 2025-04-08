package com.logging.framework.appender;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the log file lifecycle.
 * Handles file creation, rotation, and cleanup.
 */
public class LogFileManager {
    
    private static final Logger log = LoggerFactory.getLogger(LogFileManager.class);
    
    private final String logFilePath;
    private final String logFileName;
    private final File logFile;
    
    /**
     * Create a new LogFileManager.
     * 
     * @param path The path to the log file directory
     * @param filename The name of the log file
     */
    public LogFileManager(String path, String filename) {
        this.logFilePath = path;
        this.logFileName = filename;
        
        // Create the log directory if it doesn't exist
        File logDir = new File(path);
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                log.info("Created log directory: {}", path);
            } else {
                log.error("Failed to create log directory: {}", path);
            }
        }
        
        // Create the log file if it doesn't exist
        this.logFile = new File(logDir, filename);
        if (!logFile.exists()) {
            try {
                if (logFile.createNewFile()) {
                    log.info("Created log file: {}", logFile.getAbsolutePath());
                } else {
                    log.error("Failed to create log file: {}", logFile.getAbsolutePath());
                }
            } catch (IOException e) {
                log.error("Error creating log file: {}", logFile.getAbsolutePath(), e);
            }
        }
    }
    
    /**
     * Get the absolute path to the log file.
     * 
     * @return The absolute path to the log file
     */
    public String getLogFilePath() {
        return logFile.getAbsolutePath();
    }
    
    /**
     * Get the log file.
     * 
     * @return The log file
     */
    public File getLogFile() {
        return logFile;
    }
    
    /**
     * Check if the log file exists.
     * 
     * @return True if the log file exists, false otherwise
     */
    public boolean logFileExists() {
        return logFile.exists();
    }
    
    /**
     * Create the log file if it doesn't exist.
     * 
     * @return True if the log file was created or already exists, false otherwise
     */
    public boolean createLogFileIfNotExists() {
        if (!logFile.exists()) {
            try {
                return logFile.createNewFile();
            } catch (IOException e) {
                log.error("Error creating log file: {}", logFile.getAbsolutePath(), e);
                return false;
            }
        }
        return true;
    }
}
