FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/address-book-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} address-book-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/address-book-0.0.1-SNAPSHOT.jar"]