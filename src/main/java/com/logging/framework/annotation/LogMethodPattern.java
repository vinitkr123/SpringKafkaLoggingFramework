package com.logging.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify a method pattern for logging.
 * This annotation can be used at the class level to specify method patterns to include or exclude from logging.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethodPattern {
    
    /**
     * Method patterns to include in logging.
     * Patterns can include wildcards (*).
     * Example: "process*", "handle*", "save*"
     * 
     * @return Array of method patterns to include
     */
    String[] value() default {};
    
    /**
     * Method patterns to exclude from logging.
     * Patterns can include wildcards (*).
     * Example: "get*", "is*", "toString"
     * 
     * @return Array of method patterns to exclude
     */
    String[] exclude() default {};
    
    /**
     * Log level to use for the matched methods.
     * Default is INFO.
     * 
     * @return The log level
     */
    String level() default "INFO";
}
