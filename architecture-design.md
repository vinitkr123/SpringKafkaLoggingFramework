# Spring Kafka Logging Framework - Architecture Design

## Overall Architecture

The Spring Kafka Logging Framework will be structured as follows:

```
com.logging.framework
├── annotation
│   ├── EnableKafkaLogging.java
│   ├── LogKafkaConsumer.java
│   └── LogMethod.java
├── aspect
│   ├── KafkaConsumerLoggingAspect.java
│   ├── PredefinedMethodLoggingAspect.java
│   └── CustomMethodLoggingAspect.java
├── config
│   ├── KafkaLoggingAutoConfiguration.java
│   └── KafkaLoggingProperties.java
├── exception
│   ├── KafkaExceptionHandler.java
│   └── LoggingErrorHandler.java
├── model
│   ├── LoggingEvent.java
│   └── KafkaMessageContext.java
└── service
    ├── LoggingService.java
    └── LoggingServiceImpl.java
```

## Core Components

### 1. Annotations

- **EnableKafkaLogging**: Main annotation to enable the framework in a Spring application
- **LogKafkaConsumer**: Annotation to mark Kafka consumer methods for logging
- **LogMethod**: Annotation to mark custom methods for logging

### 2. Aspects

- **KafkaConsumerLoggingAspect**: Intercepts Kafka consumer methods to log message processing
- **PredefinedMethodLoggingAspect**: Intercepts predefined methods based on configuration
- **CustomMethodLoggingAspect**: Intercepts methods annotated with @LogMethod

### 3. Configuration

- **KafkaLoggingAutoConfiguration**: Spring Boot auto-configuration for the framework
- **KafkaLoggingProperties**: Configuration properties for the framework

### 4. Exception Handling

- **KafkaExceptionHandler**: Handles and logs exceptions from Kafka consumer methods
- **LoggingErrorHandler**: Implementation of ErrorHandler for Kafka listener container

### 5. Models

- **LoggingEvent**: Model representing a logging event
- **KafkaMessageContext**: Model containing Kafka message details

### 6. Services

- **LoggingService**: Interface for logging services
- **LoggingServiceImpl**: Implementation of logging service

## Integration Flow

1. **Framework Initialization**:
   - Spring Boot auto-configuration detects @EnableKafkaLogging
   - Registers aspects, exception handlers, and services

2. **Kafka Consumer Logging**:
   - KafkaConsumerLoggingAspect intercepts @KafkaListener methods
   - Extracts Kafka message details
   - Logs before and after message processing
   - Captures processing time and result

3. **Predefined Method Logging**:
   - PredefinedMethodLoggingAspect intercepts methods defined in configuration
   - Logs method execution details

4. **Custom Method Logging**:
   - CustomMethodLoggingAspect intercepts methods annotated with @LogMethod
   - Logs method execution based on annotation parameters

5. **Exception Handling**:
   - KafkaExceptionHandler captures exceptions from Kafka consumers
   - LoggingErrorHandler handles errors in Kafka listener container
   - Logs exception details with Kafka message context

## Configuration Options

### Auto-Configuration

The framework will use Spring Boot's auto-configuration mechanism to minimize setup:

```java
@Configuration
@ConditionalOnClass(KafkaListener.class)
@EnableConfigurationProperties(KafkaLoggingProperties.class)
@AutoConfigureAfter(KafkaAutoConfiguration.class)
public class KafkaLoggingAutoConfiguration {
    // Bean definitions
}
```

### Properties Configuration

Configuration via application.properties/yaml:

```yaml
kafka:
  logging:
    enabled: true
    predefined-methods:
      - com.example.service.*Service.process*
      - com.example.kafka.*Consumer.consume*
    log-level: INFO
    include-payload: true
    mask-sensitive-data: true
    async-logging: true
```

### Annotation-Based Configuration

For consumer applications that want to add custom logging:

```java
@EnableKafkaLogging
@SpringBootApplication
public class ConsumerApplication {
    // Application code
}

@Service
public class CustomService {
    @LogMethod(level = "DEBUG", includeArgs = true, includeResult = true)
    public void customMethod(String arg) {
        // Method implementation
    }
}
```

## AOP Implementation

The framework will use Spring AOP to intercept method calls:

```java
@Aspect
@Component
@ConditionalOnProperty(name = "kafka.logging.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConsumerLoggingAspect {
    
    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object logKafkaConsumer(ProceedingJoinPoint joinPoint) throws Throwable {
        // Logging implementation
    }
}
```

## Exception Handling Implementation

For exception handling, the framework will provide:

```java
@Component
public class LoggingErrorHandler implements ErrorHandler {
    
    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
        // Exception logging implementation
    }
}
```

## Performance Considerations

- Asynchronous logging for high-throughput applications
- Configurable log levels to control verbosity
- Conditional logging based on message content or context

## Security Considerations

- Data masking for sensitive information
- Configurable payload logging
- Compliance with data protection regulations
