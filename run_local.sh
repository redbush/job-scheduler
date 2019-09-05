#!/bin/bash

echo "Building application..."
./mvnw clean install -DskipTests

echo "Building images..."
docker-compose build

echo "Starting containers..."
docker-compose up