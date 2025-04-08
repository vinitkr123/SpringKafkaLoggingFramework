package com.logging.framework.aspect;

import com.logging.framework.annotation.LogKafkaConsumer;
import com.logging.framework.model.KafkaMessageContext;
import com.logging.framework.model.LoggingEvent;
import com.logging.framework.service.LoggingService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Aspect for logging Kafka consumer methods.
 * Intercepts methods annotated with @KafkaListener to log message processing.
 */
@Aspect
@Component
public class KafkaConsumerLoggingAspect {
    
    @Autowired
    private LoggingService loggingService;
    
    /**
     * Intercept Kafka listener methods.
     * This pointcut targets methods annotated with @KafkaListener.
     * 
     * @param joinPoint The join point
     * @return The result of the method execution
     * @throws Throwable If an error occurs during method execution
     */
    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener) || @annotation(com.logging.framework.annotation.LogKafkaConsumer)")
    public Object logKafkaConsumer(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        
        // Create logging event
        LoggingEvent event = new LoggingEvent();
        event.setClassName(className);
        event.setMethodName(methodName);
        event.setArguments(args);
        
        // Extract Kafka message context
        KafkaMessageContext kafkaMessageContext = extractKafkaMessageContext(method, args);
        event.setKafkaMessageContext(kafkaMessageContext);
        
        // Set log level from annotation if present
        LogKafkaConsumer annotation = method.getAnnotation(LogKafkaConsumer.class);
        if (annotation != null) {
            event.setLogLevel(annotation.level());
        } else {
            event.setLogLevel("INFO");
        }
        
        // Log method entry
        loggingService.logMethodEntry(className, methodName, args);
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        
        try {
            // Execute the method
            result = joinPoint.proceed();
            return result;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Update event with result and execution time
            event.setResult(result);
            event.setExecutionTimeMs(executionTime);
            
            // Log Kafka consumer event
            loggingService.logKafkaConsumerEvent(event);
            
            // Log method exit
            loggingService.logMethodExit(className, methodName, result, executionTime);
        }
    }
    
    /**
     * Extract Kafka message context from method arguments.
     * 
     * @param method The method being executed
     * @param args The method arguments
     * @return The Kafka message context
     */
    private KafkaMessageContext extractKafkaMessageContext(Method method, Object[] args) {
        KafkaMessageContext context = new KafkaMessageContext();
        
        // Get method parameters and annotations
        Parameter[] parameters = method.getParameters();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        // Extract information from parameters
        for (int i = 0; i < parameters.length; i++) {
            if (i >= args.length) {
                break;
            }
            
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            
            // Check for @Payload annotation
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof Payload) {
                    context.setPayload(arg);
                    break;
                }
            }
            
            // Check for @Headers annotation
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof Headers && arg instanceof Map) {
                    MessageHeaderAccessor headerAccessor = new MessageHeaderAccessor();
                    headerAccessor.copyHeaders((Map<String, Object>) arg);
                    context = KafkaMessageContext.fromMessageHeaders(headerAccessor.getMessageHeaders(), context.getPayload());
                    break;
                }
            }
            
            // Check for Acknowledgment
            if (arg instanceof Acknowledgment) {
                // Nothing to extract from Acknowledgment
                continue;
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
