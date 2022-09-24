FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
RUN mkdir app
ARG JAR_FILE
ADD /target/${JAR_FILE} /app/spring-docker-spotify.jar
WORKDIR /app
ENTRYPOINT java -jar spring-docker-spotify.jar