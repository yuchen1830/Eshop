FROM eclipse-temurin:20-jdk-alpine
WORKDIR /app
COPY target/EShop-1.0-SNAPSHOT.jar EShop-1.0-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","EShop-1.0-SNAPSHOT.jar"]