# Overview

This is a Microservice which reads csv files containing cryptocurrency data. 

The data consists of a timestamp, symbol and the price.

The service conducts various calculations to help you decide where to invest.

# Features

Fetch historical cryptocurrency data

Calculate normalized range for each crypto

Retrieve min/max/oldest/newest values for specific cryptos

Find the highest normalized range for a given day

Caching with Redis for improved performance

OpenAPI 3 documentation with Swagger UI

Changes the files while the application is running will reflect on the endpoints (updated in Redis)

# Installation & setup

JDK 17+

Maven 3+

Docker

Redis (Use Docker)

Postman or curl (to test the endpoints)

# API Endpoints

GET /cryptos/normalized

GET /cryptos/normalized/{day}

GET /cryptos/details/{cryptoName}

To receive the openapi yml use the below curl

curl http://localhost/8080/v3/api-docs.yaml -o openapi.yml

Each user is limited to 10 requests per minute based on their IP address

# Run
mvn clean install

docker-compose up