package com.logging.framework.aspect;

import com.logging.framework.config.KafkaLoggingProperties;
import com.logging.framework.model.LoggingEvent;
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
        
        // Log method entry
        loggingService.logMethodEntry(simpleClassName, methodName, args);
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        
        try {
            // Execute the method
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            // Set exception in event
            event.setException(throwable);
            throw throwable;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Update event with result and execution time
            event.setResult(result);
            event.setExecutionTimeMs(executionTime);
            
            // Log the event
            loggingService.logEvent(event);
            
            // Log method exit
            loggingService.logMethodExit(simpleClassName, methodName, result, executionTime);
        }
    }
    
    /**
     * Check if a method is predefined for logging.
     * 
     * @param fullMethodName The full method name (className.methodName)
     * @return True if the method is predefined, false otherwise
     */
    private boolean isMethodPredefined(String fullMethodName) {
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
}
