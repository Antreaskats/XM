FROM openjdk:17-jdk-slim

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

ARG JAR_FILE=target/cryptoInvestment-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar

WORKDIR /app

COPY src/main/resources/application.yml /app/resources/application.yml

ENV SPRING_CONFIG_LOCATION=/app/resources/application.yml

EXPOSE 8080 5005

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

