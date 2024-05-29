#!/bin/bash

JAR_FILE=target/user-app-0.0.1-SNAPSHOT.jar

./mvnw clean

./mvnw package -DskipTests

if [ ! -f "$JAR_FILE" ]; then
    echo "Build failed: JAR file not found!"
    exit 1
fi

docker-compose up --build
