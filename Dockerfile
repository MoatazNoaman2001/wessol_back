FROM ubuntu:latest AS build
# Install necessary dependencies
RUN apt-get update && apt-get install -y \
    wget \
    tar \
    && apt-get clean

# Download and install OpenJDK 21
RUN wget https://download.java.net/java/early_access/jdk21/9/GPL/openjdk-21_linux-x64_bin.tar.gz \
    && tar -xvf openjdk-21_linux-x64_bin.tar.gz \
    && mv jdk-21 /usr/local/ \
    && rm openjdk-21_linux-x64_bin.tar.gz \
    && update-alternatives --install /usr/bin/java java /usr/local/jdk-21/bin/java 1 \
    && update-alternatives --install /usr/bin/javac javac /usr/local/jdk-21/bin/javac 1

# Verify the installation
RUN java -version

# Set the JAVA_HOME environment variable
ENV JAVA_HOME /usr/local/jdk-21

# Add JAVA_HOME to the PATH
ENV PATH $JAVA_HOME/bin:$PATH
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