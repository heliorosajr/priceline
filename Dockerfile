FROM openjdk:17-jdk-alpine

COPY target/role-0.0.1-SNAPSHOT.jar role-api.jar

ENTRYPOINT ["java", "-jar", "role-api.jar"]