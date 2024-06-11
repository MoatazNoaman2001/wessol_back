FROM ubuntu:latest AS build
RUN apt-get install openjdk-21-jdk -y
RUN apt-get update
COPY . .
RUN ./mvnw package

FROM openjdk:21-jdk
VOLUME /tmp
EXPOSE 8080
COPY --from=build /build/libs/app-0.0.2.jar app.jar
#COPY target/app-0.0.2.jar app-0.0.2.jar
#ARG JAR_FILE=target/app-0.0.2.jar
#ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]