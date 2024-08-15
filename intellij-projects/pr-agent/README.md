# Stock Ticker Application

This project is an example stock ticker application designed to demonstrate the usage of PR-Agent. The application is built using Spring Boot and provides basic CRUD operations for managing stock information.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Endpoints](#endpoints)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Stock Ticker Application allows users to perform the following operations:
- Retrieve stock information by ID
- Retrieve stock information by ticker symbol
- Retrieve all stocks
- Create a new stock

## Architecture

The application is structured as follows:

- `StockController`: Handles HTTP requests and maps them to appropriate service calls.
- `StockRepository`: Interface for CRUD operations on the `Stock` entity.
- `Stock`: Entity representing a stock with fields such as `id`, `companyName`, `tickerSymbol`, and `price`.
- `DataInitializer`: Initializes the database with a sample stock entry when the application starts.
- `PrAgentApplication`: Main class to run the Spring Boot application.

# Endpoints

The application exposes the following RESTful endpoints:

- `GET /stocks/{id}`: Retrieve stock information by ID.
- `GET /stocks/ticker/{tickerSymbol}`: Retrieve stock information by ticker symbol.
- `GET /stocks`: Retrieve all stocks.
- `POST /stocks`: Create a new stock.

## Setup and Installation

### Prerequisites

- Java 21 or higher
- Gradle

### Steps

1. Clone the repository:
    ```sh
    git clone git@github.com:Codium-ai/devlabs.git
    cd devlabs/intellij-projects/pr-agent
    ```

2. Build the project:
    ```sh
    gradlew clean install
    ```

3. Run the application:
    ```sh
    gradlew spring-boot:run
    ```

The application will start on `http://localhost:8080`

## Usage

### Retrieve All Stocks
```sh
curl -X GET http://localhost:8080/stocks
```

### Retrieve Stock by ID
```sh
curl -X GET http://localhost:8080/stocks/{id}
```
### Retrieve Stock by TickerSymbol
```sh
curl -X GET http://localhost:8080/stocks/ticker/{tickerSymbol}
```

### Create a New Stock
```sh
curl -X POST http://localhost:8080/stocks -H "Content-Type: application/json" -d '{
  "id" : 2,
  "companyName": "New Corp",
  "tickerSymbol": "NEW",
  "price": 150.0
}'
```