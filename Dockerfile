FROM ubuntu:latest AS build
RUN apt-get install openjdk-21-jdk -y
RUN apt-get update
COPY . .
RUN ./mvnw clean package

FROM openjdk:21-jdk
VOLUME /tmp
EXPOSE 8080
COPY --from=build /build/libs/app-0.0.2.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]