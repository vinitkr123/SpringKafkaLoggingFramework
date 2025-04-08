# Spring Kafka Logging Framework - Enhanced Version

A comprehensive logging framework for Spring Kafka applications that provides non-invasive logging of Kafka consumer operations, method execution status tracking, and configurable method selection.

## Features

- **Non-invasive logging**: Log Kafka consumer operations without modifying the consumer application code
- **Dedicated log file**: Generate a separate log file specifically for Kafka operations and method executions
- **Method status tracking**: Track and log method execution status (PASSED/FAILED)
- **Configurable method selection**: Specify which methods to log through configuration
- **Predefined function logging**: Log predefined functions based on patterns
- **Custom function logging**: Add custom logging through annotations
- **Exception handling**: Capture and log exceptions from consumer applications
- **Flexible configuration**: Configure the framework through properties or annotations

## Getting Started

### Prerequisites

- Java 8 or higher
- Spring Boot 2.x or higher
- Spring Kafka

### Installation

Add the dependency to your Maven project:

```xml
<dependency>
    <groupId>com.logging.framework</groupId>
    <artifactId>spring-kafka-logging-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

1. Enable the framework in your Spring Boot application:

```java
@SpringBootApplication
@EnableKafkaLogging
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

2. Configure the framework in your `application.properties` or `application.yml`:

```properties
# Kafka Logging Framework Configuration
kafka.logging.enabled=true
kafka.logging.log-level=INFO

# Log File Configuration
kafka.logging.log-file.enabled=true
kafka.logging.log-file.path=./logs
kafka.logging.log-file.filename=kafka-consumer.log
```

3. That's it! The framework will automatically log Kafka consumer operations and method executions based on the configuration.

## Configuration Options

### Main Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `kafka.logging.enabled` | Enable/disable the logging framework | `true` |
| `kafka.logging.log-level` | Log level for logging events | `INFO` |
| `kafka.logging.include-payload` | Include message payloads in logs | `true` |
| `kafka.logging.mask-sensitive-data` | Mask sensitive data in logs | `true` |
| `kafka.logging.async-logging` | Use asynchronous logging | `true` |
| `kafka.logging.sensitive-fields` | List of sensitive field names to mask | `password,creditCard,ssn` |

### Log File Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `kafka.logging.log-file.enabled` | Enable/disable dedicated log file | `true` |
| `kafka.logging.log-file.path` | Path to the log file directory | `./logs` |
| `kafka.logging.log-file.filename` | Name of the log file | `kafka-logging.log` |
| `kafka.logging.log-file.max-size` | Maximum size of the log file before rotation | `10MB` |
| `kafka.logging.log-file.max-history` | Maximum number of log files to keep | `7` |
| `kafka.logging.log-file.pattern` | Log pattern for the file | `[%d{yyyy-MM-dd HH:mm:ss}] [%p] [%X{status}] [%X{class}#%X{method}] - %m%n` |

### Method Selection Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `kafka.logging.method-selection.enabled` | Enable/disable method selection | `true` |
| `kafka.logging.method-selection.log-all-by-default` | Log all methods by default | `false` |
| `kafka.logging.method-selection.log-exceptions` | Log methods that throw exceptions | `true` |
| `kafka.logging.method-selection.include-method-patterns` | Method patterns to include | `process*,handle*,consume*` |
| `kafka.logging.method-selection.exclude-method-patterns` | Method patterns to exclude | `get*,set*,is*` |
| `kafka.logging.method-selection.include-class-patterns` | Class patterns to include | `*Service,*Consumer,*Handler` |
| `kafka.logging.method-selection.include-package-patterns` | Package patterns to include | `com.example.consumer` |

## Advanced Usage

### Enabling the Framework with Options

You can customize the framework behavior using the `@EnableKafkaLogging` annotation:

```java
@SpringBootApplication
@EnableKafkaLogging(
    enableMethodSelection = true,
    enableLogFile = true,
    enableStatusTracking = true
)
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### Logging Specific Methods

You can use the `@LogMethodPattern` annotation to specify which methods to log in a class:

```java
@Service
@LogMethodPattern(
    value = {"process*", "handle*", "save*"},
    exclude = {"getConfig", "isEnabled"}
)
public class MyService {
    // Methods matching the patterns will be logged
}
```

### Logging Individual Methods

You can use the `@LogMethod` annotation to log specific methods:

```java
@Service
public class MyService {
    
    @LogMethod(
        includeArgs = true,
        includeResult = true,
        logExecutionTime = true,
        description = "Processing important data"
    )
    public Result processData(Data data) {
        // Method implementation
    }
}
```

## Method Status Tracking

The framework tracks the execution status of methods and logs it in the dedicated log file:

- **IN_PROGRESS**: Method execution has started
- **PASSED**: Method execution completed successfully
- **FAILED**: Method execution failed with an exception

Example log entries:

```
[2025-04-08 02:30:15] [INFO] [PASSED] [KafkaConsumerService#processMessage] - Method executed successfully | Duration: 15 ms
[2025-04-08 02:30:20] [ERROR] [FAILED] [KafkaConsumerService#saveMessage] - Method execution failed | Exception: RuntimeException: Error saving message
```

## Exception Handling

The framework automatically captures and logs exceptions from consumer applications:

1. When a method throws an exception, it's marked as FAILED
2. The exception details are logged to the dedicated log file
3. The original exception is re-thrown to allow normal application error handling

## Sample Application

A sample application is included in the repository to demonstrate the framework's features:

1. Clone the repository
2. Build the framework and sample application:
   ```
   ./build.sh
   ```
3. Run the sample application:
   ```
   java -jar sample-kafka-consumer/target/sample-kafka-consumer-0.0.1-SNAPSHOT.jar
   ```
4. Use the test endpoints to send messages:
   ```
   curl -X POST "http://localhost:8080/api/test/send?content=test-message"
   curl -X POST "http://localhost:8080/api/test/send-error?content=error-message"
   ```
5. Check the logs in the `./logs` directory

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
