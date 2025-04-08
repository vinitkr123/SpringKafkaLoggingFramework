# Spring Kafka Logging Framework

A non-invasive logging framework for Spring Kafka applications that logs Kafka consumer operations without requiring code modifications in the consuming applications.

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Requirements](#requirements)
4. [Installation](#installation)
5. [Usage](#usage)
6. [Configuration](#configuration)
7. [Advanced Usage](#advanced-usage)
8. [Sample Application](#sample-application)
9. [Contributing](#contributing)

## Overview

Spring Kafka Logging Framework is designed to provide comprehensive logging for Kafka consumer applications without requiring changes to the consumer's codebase. It uses Spring AOP to intercept method calls and automatically log Kafka operations, custom methods, and exceptions.

## Features

- **Non-invasive Integration**: Works without modifying consumer application code
- **Predefined Function Logging**: Automatically logs data from predefined functions
- **Custom Function Logging**: Configurable logging for additional functions
- **Exception Handling**: Comprehensive logging of exceptions
- **Configurable**: Extensive configuration options via properties or annotations
- **Spring Boot Integration**: Seamless integration with Spring Boot applications

## Requirements

- Java 8 or higher
- Spring Framework 5.x or higher
- Spring Boot 2.x or higher
- Spring Kafka 2.x or higher

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.logging.framework</groupId>
    <artifactId>spring-kafka-logging-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle`:

```groovy
implementation 'com.logging.framework:spring-kafka-logging-framework:1.0.0'
```

## Usage

### Basic Setup

1. Add the dependency to your project
2. Enable the framework by adding `@EnableKafkaLogging` to your main application class:

```java
@EnableKafkaLogging
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

That's it! The framework will automatically log:
- All Kafka consumer methods (annotated with `@KafkaListener`)
- Methods matching predefined patterns in configuration
- Methods explicitly annotated with `@LogMethod`
- Exceptions thrown during Kafka message processing

### Logging Kafka Consumer Methods

Kafka consumer methods are automatically logged without any additional configuration:

```java
@Service
public class MyConsumerService {
    
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void processMessage(MyMessage message) {
        // This method will be automatically logged
        // No additional code needed
    }
}
```

### Logging Custom Methods

To log custom methods beyond the predefined set, use the `@LogMethod` annotation:

```java
@Service
public class MyService {
    
    @LogMethod(level = "DEBUG", includeArgs = true, includeResult = true)
    public String processData(String data) {
        // This method will be logged with the specified options
        return data.toUpperCase();
    }
}
```

## Configuration

### Application Properties

Configure the framework using the following properties in `application.properties` or `application.yml`:

```properties
# Enable/disable the framework
kafka.logging.enabled=true

# Predefined method patterns to log
kafka.logging.predefined-methods[0]=com.example.service.*Service.process*
kafka.logging.predefined-methods[1]=com.example.kafka.*Consumer.consume*

# Log level for logging events
kafka.logging.log-level=INFO

# Include message payloads in logs
kafka.logging.include-payload=true

# Mask sensitive data in logs
kafka.logging.mask-sensitive-data=true

# Sensitive fields to mask
kafka.logging.sensitive-fields[0]=password
kafka.logging.sensitive-fields[1]=creditCard

# Use asynchronous logging
kafka.logging.async-logging=true
```

### Annotation Options

#### @EnableKafkaLogging

Main annotation to enable the framework:

```java
@EnableKafkaLogging
@SpringBootApplication
public class MyApplication {
    // ...
}
```

#### @LogKafkaConsumer

Optional annotation for Kafka consumer methods with additional options:

```java
@LogKafkaConsumer(level = "DEBUG", includePayload = true, includeHeaders = true)
@KafkaListener(topics = "my-topic")
public void processMessage(MyMessage message) {
    // ...
}
```

Options:
- `level`: Log level (default: "INFO")
- `includePayload`: Whether to include the message payload (default: true)
- `includeHeaders`: Whether to include message headers (default: true)
- `logProcessingTime`: Whether to log processing time (default: true)

#### @LogMethod

Annotation for custom methods:

```java
@LogMethod(level = "DEBUG", includeArgs = true, includeResult = true, description = "Process data")
public String processData(String data) {
    // ...
}
```

Options:
- `level`: Log level (default: "INFO")
- `includeArgs`: Whether to include method arguments (default: true)
- `includeResult`: Whether to include method result (default: true)
- `logExecutionTime`: Whether to log execution time (default: true)
- `description`: Custom description to include in the log

## Advanced Usage

### Custom Error Handling

The framework provides built-in error handling for Kafka consumers, but you can customize it by defining your own error handler:

```java
@Bean
public ErrorHandler customErrorHandler(LoggingService loggingService) {
    return new CustomLoggingErrorHandler(loggingService);
}
```

### Masking Sensitive Data

Configure sensitive fields to be masked in logs:

```properties
kafka.logging.mask-sensitive-data=true
kafka.logging.sensitive-fields[0]=password
kafka.logging.sensitive-fields[1]=creditCard
kafka.logging.masking-char=*
```

### Asynchronous Logging

For high-throughput applications, enable asynchronous logging:

```properties
kafka.logging.async-logging=true
```

## Sample Application

A sample application demonstrating the framework is included in the `sample-kafka-consumer` directory. It shows:

1. Basic setup with `@EnableKafkaLogging`
2. Automatic logging of Kafka consumer methods
3. Predefined method logging based on configuration
4. Custom method logging with `@LogMethod`
5. Exception handling and logging

To run the sample application:

```bash
cd sample-kafka-consumer
./mvnw spring-boot:run
```

Test endpoints:
- Send normal message: `GET /api/test/send?content=test&sender=user`
- Send error message: `GET /api/test/send-error?content=test&sender=user`

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
