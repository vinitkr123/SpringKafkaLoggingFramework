package com.example.consumer.model;

/**
 * Sample model class representing a message from Kafka.
 */
public class KafkaMessage {
    
    private String id;
    private String content;
    private String sender;
    private long timestamp;
    
    public KafkaMessage() {
    }
    
    public KafkaMessage(String id, String content, String sender, long timestamp) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSender() {
        return sender;
    }
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "KafkaMessage{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
