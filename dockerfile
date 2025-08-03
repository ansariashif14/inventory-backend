From openjdk:17-jdk-alpine 
COPY target/inventory-backend-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]