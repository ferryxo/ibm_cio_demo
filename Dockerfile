FROM openjdk:11-jre-slim
MAINTAINER "Ferry App <ferryxo@gmail.com>"
WORKDIR /app

COPY ./target/*.jar ./app.jar
COPY ./target/classes/input.json ./input.json
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8081