package com.logging.framework.exception;

import com.logging.framework.model.KafkaMessageContext;
import com.logging.framework.service.LoggingService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Aspect for handling exceptions in Kafka consumer methods.
 * Intercepts exceptions thrown by methods and logs them.
 */
@Aspect
@Component
public class KafkaExceptionHandler {
    
    @Autowired
    private LoggingService loggingService;
    
    /**
     * Handle exceptions thrown by Kafka listener methods.
     * This advice is triggered after an exception is thrown by a method annotated with @KafkaListener.
     * 
     * @param joinPoint The join point where the exception was thrown
     * @param exception The exception that was thrown
     */
    @AfterThrowing(pointcut = "@annotation(org.springframework.kafka.annotation.KafkaListener)", throwing = "exception")
    public void handleKafkaListenerException(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        
        // Log the exception
        loggingService.logException(className, methodName, exception, args);
        
        // Try to extract Kafka message context if possible
        KafkaMessageContext context = extractKafkaMessageContext(method, args);
        if (context != null && context.getTopic() != null) {
            // Log additional information about the Kafka message
            loggingService.logException("KafkaConsumer", "onMessage", exception, new Object[]{context});
        }
    }
    
    /**
     * Handle exceptions thrown by any method.
     * This advice is triggered after an exception is thrown by any method.
     * 
     * @param joinPoint The join point where the exception was thrown
     * @param exception The exception that was thrown
     */
    @AfterThrowing(pointcut = "execution(* *(..))", throwing = "exception")
    public void handleGeneralException(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        
        // Only log if not already handled by handleKafkaListenerException
        if (method.getAnnotation(KafkaListener.class) == null) {
            loggingService.logException(className, methodName, exception, args);
        }
    }
    
    /**
     * Extract Kafka message context from method arguments.
     * 
     * @param method The method being executed
     * @param args The method arguments
     * @return The Kafka message context or null if not available
     */
    private KafkaMessageContext extractKafkaMessageContext(Method method, Object[] args) {
        KafkaMessageContext context = new KafkaMessageContext();
        
        // Get method parameters and annotations
        Parameter[] parameters = method.getParameters();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        // Extract information from parameters
        for (int i = 0; i < parameters.length; i++) {
            if (i >= args.length || args[i] == null) {
                continue;
            }
            
            Object arg = args[i];
            
            // Check for @Payload annotation
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof Payload) {
                    context.setPayload(arg);
                    break;
                }
            }
            
            // If no payload has been set yet, use the first non-null argument
            if (context.getPayload() == null) {
                context.setPayload(arg);
            }
        }
        
        // Extract topic from KafkaListener annotation
        KafkaListener kafkaListener = method.getAnnotation(KafkaListener.class);
        if (kafkaListener != null && kafkaListener.topics().length > 0) {
            context.setTopic(String.join(",", kafkaListener.topics()));
        }
        
        return context;
    }
}
