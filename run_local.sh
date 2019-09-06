#!/bin/bash

echo "Building applications..."
./mvnw.sh clean install -DskipTests

echo "Building images..."
docker-compose rm -fsv
docker-compose build

echo "Starting containers..."
docker-compose up