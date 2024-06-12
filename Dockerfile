FROM ubuntu:latest AS build
# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64/
RUN export JAVA_HOME
RUN apt-get update
COPY . .
RUN ./mvnw clean package

FROM openjdk:21-jdk
VOLUME /tmp
EXPOSE 8080
COPY --from=build /target/app-0.0.2.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]