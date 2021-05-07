FROM openjdk:11-jre-slim
MAINTAINER "Ferry App <ferryxo@gmail.com>"
WORKDIR /app

COPY ./target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8081