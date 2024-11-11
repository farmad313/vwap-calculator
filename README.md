## Read Me First

#### ✔️ VWAP Calculator
This project is an implementation of the Volume Weighted Average Price (VWAP) calculator for production.
Application is built using Java 21, Spring Boot 3, Kafka, and H2 Database.

The application has 3 main components:
1. **Kafka Producer**: The Kafka producer simulates a price feed for a list of currency pairs. It generates random dummy price data and sends it to a Kafka topic.
2. **Kafka Stream Consumer**: The Kafka consumer reads and process the stream of price data  from the Kafka topic, calculates the VWAP for each currency pair every minute, and stores the result in an in-memory H2 database.
3. **Final Calculator**: The final calculation component query H2 DB and retrieve last hour VWAP, and calculate the final VWAP for each currency pair evey hour.


#### ✔️ Prerequisites
- Java 21
- Maven
- Docker

#### ✔️ Setup
The application uses the following default configurations in application.yaml and can be updated as needed:

- This one is for Kafka Consumer and says that the application calculates the VWAP for each currency pair every 1 minute:

- `intermediate-processor.window-duration-minutes: 1`

- This one is for final calculator and says that the application calculates the final VWAP for each currency pair every 1 hour which is 3600000 milliseconds, and it can be updated to 120000  which is 2 minutes for testing purposes: 

- `final-processor.interval: 3600000`

#### ✔️ Build and Run
- Clone the repository
- Run the following command to build the project
```shell
mvnw clean install
```
- Run the following command to start Kafka, Zookeeper, and Kafka UI:
```shell
docker-compose up
```
- Run the following command to start the application:
```shell
java -jar target/vwap-calculator-0.0.1-SNAPSHOT.jar
```

#### ✔️ URLs to monitor the application functionality:
- Kafka UI:
http://localhost:8090/

- H2 Database Console:
http://localhost:8080/h2-console

- API Endpoints:
The application also provides simple REST APIs to retrieve the VWAP data in H2 DB:
 ```shell
GET http://localhost:8080/vwap-calculator/v1/aggregateresult
GET http://localhost:8080/vwap-calculator/v1/aggregateresult/recent?minutes=60
GET http://localhost:8080/vwap-calculator/v1/aggregateresult/recent/vwap?minutes=60
```

---

## Software Design and Architecture Overview

#### ✔️ Is it mathematically feasible to aggregate the sum of VWAP?
Yes, if we store these minute VWAPs and total volume. We can calculate the final VWAP for each currency pair every hour by summing the minute VWAPs and total volume for the last 60 minutes and dividing by the total volume.

#### ✔️ What is the best way to calculate the VWAP?
1. Calculate the VWAP for each currency pair every minute using the formula:
   VWAP = (Sum of Price * Volume) / Total Volume
2. Store the minute VWAP and total volume for each currency pair in an in-memory database like H2.
   3. Calculate the final VWAP for each currency pair every hour by summing the minute VWAPs and total volume for the last 60 minutes and dividing by the total volume.
   VWAP = (Sum of Minute VWAP * Total Volume) / Total Volume

#### ✔️ What is the best Data type for Price and Volume and VWAP calculation?
1. BigDecimal: Use BigDecimal for prices and volumes in financial applications to ensure precise and accurate calculations.
2. Double: double might be faster and use less memory, but it is prone to rounding errors and not suitable for financial precision.

#### ✔️ What is the best way to produce and consume the price data?
1. Kafka Message Stream: Kafka provides a robust platform for handling real-time data streams. In this context, a stream of data refers to messages being produced and consumed in real-time using Kafka topics. This is ideal for scenarios requiring high throughput and fault-tolerance. Use Kafka if you need a robust solution for handling large-scale data streams, event processing, and building real-time data pipelines.
1. Server-Sent Events (SSE): SSE allows a server to push updates to the client in real-time over HTTP. This method is useful for live data feeds such as stock prices, where the server can continuously send updates to the client as new data becomes available. Use SSE if you need a straightforward way to push updates from the server to the client without requiring bidirectional communication.


#### ✔️ How do these 3 components work together and makes the application scalable and fault-tolerant?
1. The application is designed to be scalable and fault-tolerant. 
2. The application components are designed to be stateless and can be easily decomposed into separate microservice and scaled horizontally to handle a large volume of price data.
3. It uses Kafka as a message broker to decouple the producer and consumer components. 
4. The Kafka consumer uses Kafka Streams to process the stream of price data and calculate the VWAP for each currency pair every minute. The interval can be adjusted to shorter or longer periods as needed.
5. Kafka uses RocksDB as a local state store to maintain the intermediate results.
6. The consumer stores the minute VWAP and total volume for each currency pair in an in-memory H2 database. We can use more powerful distributed databases for scalability and fault-tolerance.
7. The final calculator component queries the H2 DB and calculates the final VWAP for each currency pair every hour. The interval can be adjusted to shorter or longer periods as needed.

#### ✔️ What are the other best practices for handling real-time data streams?
1. Virtual thread
2. Parallel stream


---

Developed by [Amirhossein Farmad](https://www.linkedin.com/in/amirhossein-farmad/)
