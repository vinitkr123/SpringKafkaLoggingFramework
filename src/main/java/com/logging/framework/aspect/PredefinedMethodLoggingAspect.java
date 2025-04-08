package com.logging.framework.aspect;

import com.logging.framework.config.KafkaLoggingProperties;
import com.logging.framework.model.LoggingEvent;
import com.logging.framework.model.MethodExecutionStatus;
import com.logging.framework.service.LoggingService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Aspect for logging predefined methods.
 * Intercepts methods based on configuration to provide detailed logging.
 */
@Aspect
@Component
public class PredefinedMethodLoggingAspect {
    
    @Autowired
    private LoggingService loggingService;
    
    @Autowired
    private KafkaLoggingProperties properties;
    
    /**
     * Intercept methods based on predefined patterns.
     * This pointcut uses a dynamic expression to match methods defined in configuration.
     * 
     * @param joinPoint The join point
     * @return The result of the method execution
     * @throws Throwable If an error occurs during method execution
     */
    @Around("execution(* *(..))")
    public Object logPredefinedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = signature.getDeclaringType().getName();
        String methodName = method.getName();
        String fullMethodName = className + "." + methodName;
        
        // Check if method matches any predefined pattern
        if (!isMethodPredefined(fullMethodName)) {
            // If not predefined, proceed without logging
            return joinPoint.proceed();
        }
        
        // Method is predefined, log it
        String simpleClassName = signature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        // Create logging event
        LoggingEvent event = new LoggingEvent();
        event.setClassName(simpleClassName);
        event.setMethodName(methodName);
        event.setArguments(args);
        event.setLogLevel(properties.getLogLevel());
        event.setStatus(MethodExecutionStatus.IN_PROGRESS);
        
        // Log method entry
        loggingService.logMethodEntry(simpleClassName, methodName, args);
        
        // Log initial status
        loggingService.logMethodStatus(simpleClassName, methodName, MethodExecutionStatus.IN_PROGRESS, 
                "Executing predefined method - Started");
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        
        try {
            // Execute the method
            result = joinPoint.proceed();
            
            // Set status to PASSED
            event.setStatus(MethodExecutionStatus.PASSED);
            
            return result;
        } catch (Throwable throwable) {
            // Set status to FAILED and set exception
            event.setStatus(MethodExecutionStatus.FAILED);
            event.setException(throwable);
            
            // Log failure status
            loggingService.logMethodStatus(simpleClassName, methodName, MethodExecutionStatus.FAILED, 
                    "Predefined method execution failed: " + throwable.getMessage());
            
            throw throwable;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Update event with result and execution time
            event.setResult(result);
            event.setExecutionTimeMs(executionTime);
            
            // Log the event
            loggingService.logEvent(event);
            
            // Log method exit with status
            loggingService.logMethodExit(simpleClassName, methodName, result, executionTime, event.getStatus());
            
            // Log final status if successful
            if (event.getStatus() == MethodExecutionStatus.PASSED) {
                loggingService.logMethodStatus(simpleClassName, methodName, MethodExecutionStatus.PASSED, 
                        "Predefined method executed successfully in " + executionTime + " ms");
            }
        }
    }
    
    /**
     * Check if a method is predefined for logging.
     * 
     * @param fullMethodName The full method name (className.methodName)
     * @return True if the method is predefined, false otherwise
     */
    private boolean isMethodPredefined(String fullMethodName) {
        // First check method selection configuration
        if (isMethodSelected(fullMethodName)) {
            return true;
        }
        
        // Then check legacy predefined methods
        List<String> predefinedMethods = properties.getPredefinedMethods();
        
        if (predefinedMethods == null || predefinedMethods.isEmpty()) {
            return false;
        }
        
        for (String pattern : predefinedMethods) {
            // Convert wildcard pattern to regex
            String regex = pattern
                    .replace(".", "\\.")
                    .replace("*", ".*");
            
            if (Pattern.matches(regex, fullMethodName)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if a method is selected for logging based on the enhanced configuration.
     * 
     * @param fullMethodName The full method name (className.methodName)
     * @return True if the method is selected, false otherwise
     */
    private boolean isMethodSelected(String fullMethodName) {
        KafkaLoggingProperties.MethodSelectionConfig config = properties.getMethodSelection();
        
        if (config == null) {
            return false;
        }
        
        // Check if method is explicitly excluded
        for (String pattern : config.getExcludePatterns()) {
            String regex = pattern
                    .replace(".", "\\.")
                    .replace("*", ".*");
            
            if (Pattern.matches(regex, fullMethodName)) {
                return false;
            }
        }
        
        // Check if method matches include patterns
        for (String pattern : config.getIncludePatterns()) {
            String regex = pattern
                    .replace(".", "\\.")
                    .replace("*", ".*");
            
            if (Pattern.matches(regex, fullMethodName)) {
                return true;
            }
        }
        
        // Check if method's class is included
        String className = fullMethodName.substring(0, fullMethodName.lastIndexOf('.'));
        for (String includedClass : config.getIncludeClasses()) {
            if (className.equals(includedClass)) {
                return true;
            }
        }
        
        // Check if method's package is included
        for (String includedPackage : config.getIncludePackages()) {
            if (className.startsWith(includedPackage)) {
                return true;
            }
        }
        
        return false;
    }
}
