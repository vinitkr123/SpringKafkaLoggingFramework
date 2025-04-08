#!/bin/bash

# Build script for Spring Kafka Logging Framework
# This script builds both the framework and the sample application

echo "Building Spring Kafka Logging Framework..."

# Build the main framework
cd /home/ubuntu/spring-kafka-logging-framework
mvn clean install

# Build the sample application
cd /home/ubuntu/spring-kafka-logging-framework/sample-kafka-consumer
mvn clean package

echo "Build completed successfully!"
echo "Framework JAR: /home/ubuntu/spring-kafka-logging-framework/target/spring-kafka-logging-framework-1.0.0.jar"
echo "Sample Application JAR: /home/ubuntu/spring-kafka-logging-framework/sample-kafka-consumer/target/sample-kafka-consumer-0.0.1-SNAPSHOT.jar"
