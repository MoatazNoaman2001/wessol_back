FROM openjdk:21-jdk
VOLUME /tmp
EXPOSE 8080
COPY target/app-0.0.2.jar app-0.0.2.jar
ARG JAR_FILE=target/app-0.0.2.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]