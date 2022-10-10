FROM maven:3.6.3-openjdk-11-slim AS build
RUN mkdir app
WORKDIR /app
COPY pom.xml .
COPY src src/
RUN mvn clean package --file pom.xml -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
COPY --from=build /app/target/jarTest1.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar app.jar
