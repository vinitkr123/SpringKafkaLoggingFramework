package com.logging.framework.aspect;

import com.logging.framework.annotation.LogMethod;
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

/**
 * Aspect for logging custom methods.
 * Intercepts methods annotated with @LogMethod to provide detailed logging.
 */
@Aspect
@Component
public class CustomMethodLoggingAspect {
    
    @Autowired
    private LoggingService loggingService;
    
    /**
     * Intercept methods annotated with @LogMethod.
     * 
     * @param joinPoint The join point
     * @return The result of the method execution
     * @throws Throwable If an error occurs during method execution
     */
    @Around("@annotation(com.logging.framework.annotation.LogMethod)")
    public Object logCustomMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        
        // Get annotation
        LogMethod annotation = method.getAnnotation(LogMethod.class);
        
        // Create logging event
        LoggingEvent event = new LoggingEvent();
        event.setClassName(className);
        event.setMethodName(methodName);
        event.setLogLevel(annotation.level());
        event.setStatus(MethodExecutionStatus.IN_PROGRESS);
        
        // Log arguments if configured
        if (annotation.includeArgs()) {
            event.setArguments(args);
            loggingService.logMethodEntry(className, methodName, args);
        } else {
            loggingService.logMethodEntry(className, methodName, null);
        }
        
        // Log initial status
        String description = annotation.description().isEmpty() ? 
                "Executing method" : annotation.description();
        loggingService.logMethodStatus(className, methodName, MethodExecutionStatus.IN_PROGRESS, 
                description + " - Started");
        
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
            loggingService.logMethodStatus(className, methodName, MethodExecutionStatus.FAILED, 
                    description + " - Failed: " + throwable.getMessage());
            
            throw throwable;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Set execution time
            if (annotation.logExecutionTime()) {
                event.setExecutionTimeMs(executionTime);
            }
            
            // Set result if configured
            if (annotation.includeResult()) {
                event.setResult(result);
            }
            
            // Log the event
            loggingService.logEvent(event);
            
            // Log method exit with status
            if (annotation.includeArgs() && annotation.includeResult()) {
                loggingService.logMethodExit(className, methodName, result, executionTime, event.getStatus());
            }
            
            // Log final status if successful
            if (event.getStatus() == MethodExecutionStatus.PASSED) {
                loggingService.logMethodStatus(className, methodName, MethodExecutionStatus.PASSED, 
                        description + " - Completed in " + executionTime + " ms");
            }
        }
    }
}
