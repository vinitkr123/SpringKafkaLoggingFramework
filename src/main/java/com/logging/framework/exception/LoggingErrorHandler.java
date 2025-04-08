package com.logging.framework.exception;

import com.logging.framework.model.KafkaMessageContext;
import com.logging.framework.service.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

/**
 * Implementation of Kafka's ErrorHandler interface.
 * Handles errors that occur during Kafka message processing.
 */
@Component
public class LoggingErrorHandler implements ErrorHandler {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingErrorHandler.class);
    
    @Autowired
    private LoggingService loggingService;
    
    @Override
    public void handle(Exception thrownException, org.apache.kafka.clients.consumer.ConsumerRecord<?, ?> data) {
        String topic = data.topic();
        int partition = data.partition();
        long offset = data.offset();
        Object key = data.key();
        Object value = data.value();
        
        log.error("Error while processing Kafka message: topic={}, partition={}, offset={}, key={}",
                topic, partition, offset, key, thrownException);
        
        // Create Kafka message context
        KafkaMessageContext context = new KafkaMessageContext();
        context.setTopic(topic);
        context.setPartition(partition);
        context.setOffset(offset);
        if (key instanceof String) {
            context.setKey((String) key);
        }
        context.setPayload(value);
        
        // Log exception with context
        loggingService.logException("KafkaConsumer", "onMessage", thrownException, new Object[]{context});
    }
    
    /**
     * Handle errors from Spring Kafka's message listener container.
     * This method is called when an exception is thrown during message processing.
     * 
     * @param message The message being processed
     * @param exception The exception that was thrown
     */
    public void handleListenerException(Message<?> message, Exception exception) {
        MessageHeaders headers = message.getHeaders();
        String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC, String.class);
        Integer partition = headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class);
        Long offset = headers.get(KafkaHeaders.OFFSET, Long.class);
        Object key = headers.get(KafkaHeaders.RECEIVED_KEY);
        Object payload = message.getPayload();
        
        log.error("Error while processing Kafka message: topic={}, partition={}, offset={}, key={}",
                topic, partition, offset, key, exception);
        
        // Create Kafka message context
        KafkaMessageContext context = KafkaMessageContext.fromMessageHeaders(headers, payload);
        
        // Log exception with context
        loggingService.logException("KafkaConsumer", "onMessage", exception, new Object[]{context});
    }
}
