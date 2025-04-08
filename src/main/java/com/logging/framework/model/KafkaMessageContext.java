package com.logging.framework.model;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;

/**
 * Model containing Kafka message details.
 * Captures information about the Kafka message being processed.
 */
public class KafkaMessageContext {
    
    private String topic;
    private Integer partition;
    private Long offset;
    private String key;
    private Object payload;
    private MessageHeaders headers;
    
    public KafkaMessageContext() {
    }
    
    /**
     * Create a KafkaMessageContext from MessageHeaders.
     * Extracts Kafka-specific information from the headers.
     * 
     * @param headers The message headers
     * @param payload The message payload
     * @return A new KafkaMessageContext
     */
    public static KafkaMessageContext fromMessageHeaders(MessageHeaders headers, Object payload) {
        KafkaMessageContext context = new KafkaMessageContext();
        
        if (headers != null) {
            context.setHeaders(headers);
            
            if (headers.get(KafkaHeaders.RECEIVED_TOPIC) != null) {
                context.setTopic(headers.get(KafkaHeaders.RECEIVED_TOPIC, String.class));
            }
            
            if (headers.get(KafkaHeaders.RECEIVED_PARTITION) != null) {
                context.setPartition(headers.get(KafkaHeaders.RECEIVED_PARTITION, Integer.class));
            }
            
            if (headers.get(KafkaHeaders.OFFSET) != null) {
                context.setOffset(headers.get(KafkaHeaders.OFFSET, Long.class));
            }
            
            if (headers.get(KafkaHeaders.RECEIVED_KEY) != null) {
                context.setKey(headers.get(KafkaHeaders.RECEIVED_KEY, String.class));
            }
        }
        
        context.setPayload(payload);
        
        return context;
    }
    
    // Getters and Setters
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public Integer getPartition() {
        return partition;
    }
    
    public void setPartition(Integer partition) {
        this.partition = partition;
    }
    
    public Long getOffset() {
        return offset;
    }
    
    public void setOffset(Long offset) {
        this.offset = offset;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public Object getPayload() {
        return payload;
    }
    
    public void setPayload(Object payload) {
        this.payload = payload;
    }
    
    public MessageHeaders getHeaders() {
        return headers;
    }
    
    public void setHeaders(MessageHeaders headers) {
        this.headers = headers;
    }
    
    @Override
    public String toString() {
        return "KafkaMessageContext{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                ", key='" + key + '\'' +
                '}';
    }
}
