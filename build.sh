#!/bin/bash

# Build script for Enhanced Spring Kafka Logging Framework
# This script builds both the framework and the sample application

echo "Building Enhanced Spring Kafka Logging Framework..."

# Create logs directory for tests
mkdir -p logs

# Build the main framework
cd /home/ubuntu/spring-kafka-logging-framework
mvn clean install -DskipTests

# Build the sample application
cd /home/ubuntu/spring-kafka-logging-framework/sample-kafka-consumer
mvn clean package -DskipTests

echo "Build completed successfully!"
echo "Framework JAR: /home/ubuntu/spring-kafka-logging-framework/target/spring-kafka-logging-framework-1.0.0.jar"
echo "Sample Application JAR: /home/ubuntu/spring-kafka-logging-framework/sample-kafka-consumer/target/sample-kafka-consumer-0.0.1-SNAPSHOT.jar"
echo ""
echo "To run tests:"
echo "cd /home/ubuntu/spring-kafka-logging-framework && mvn test"
echo "cd /home/ubuntu/spring-kafka-logging-framework/sample-kafka-consumer && mvn test"
echo ""
echo "To run the sample application:"
echo "java -jar /home/ubuntu/spring-kafka-logging-framework/sample-kafka-consumer/target/sample-kafka-consumer-0.0.1-SNAPSHOT.jar"
