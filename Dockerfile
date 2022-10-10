FROM maven:3.6.3-openjdk-11-slim AS buil
RUN mkdir app
WORKDIR /app
COPY pom.xml .
COPY src src/
RUN ls
RUN mvn clean package --file pom.xml -DskipTests
RUN ls
RUN ls target/

FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
EXPOSE 8080
ADD jarTest1.jar target/jarTest1.jar
ENTRYPOINT java -jar jarTest1.jar
