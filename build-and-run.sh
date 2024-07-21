#!/bin/bash

JAR_FILE=build/libs/userapp-0.0.1-SNAPSHOT.jar

./gradlew clean

./gradlew build -x test

if [ ! -f "$JAR_FILE" ]; then
    echo "Build failed: JAR file not found!"
    exit 1
fi

docker-compose up --build