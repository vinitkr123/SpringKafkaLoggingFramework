package com.logging.framework.model;

/**
 * Enum representing the execution status of methods.
 * Used to track and report method execution status in logs.
 */
public enum MethodExecutionStatus {
    /**
     * Method execution is in progress.
     */
    IN_PROGRESS,
    
    /**
     * Method execution completed successfully.
     */
    PASSED,
    
    /**
     * Method execution failed with an exception.
     */
    FAILED
}
