FROM maven:3.6.3-openjdk-11-slim AS buil
RUN mkdir app
WORKDIR /app
COPY pom.xml .
COPY src .
RUN mvn -B package --file pom.xml -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
EXPOSE 8080
ENTRYPOINT java -jar target/jarTest1.jar
