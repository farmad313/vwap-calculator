FROM openjdk:21-jdk-slim
MAINTAINER dev.amir
COPY target/vwap-calculator-0.0.1-SNAPSHOT.jar vwap-calculator-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/vwap-calculator-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080