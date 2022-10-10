FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
RUN mkdir app
ADD /target/jarTest1.jar /app/jarTest1.jar
WORKDIR /app
ENTRYPOINT java -jar jarTest1.jar
