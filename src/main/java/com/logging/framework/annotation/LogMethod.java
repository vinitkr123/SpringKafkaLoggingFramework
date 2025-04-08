package com.logging.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark custom methods for logging.
 * This annotation can be used on any method to enable detailed logging
 * of method execution beyond the predefined set of methods.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethod {
    
    /**
     * Log level for the method.
     * Default is INFO.
     */
    String level() default "INFO";
    
    /**
     * Whether to include method arguments in the logs.
     * Default is true.
     */
    boolean includeArgs() default true;
    
    /**
     * Whether to include method result in the logs.
     * Default is true.
     */
    boolean includeResult() default true;
    
    /**
     * Whether to log the execution time.
     * Default is true.
     */
    boolean logExecutionTime() default true;
    
    /**
     * Custom description to include in the log.
     */
    String description() default "";
}
