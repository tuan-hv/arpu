FROM openjdk:8-jdk-alpine
ARG JAR_FILE=arpu-be/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]