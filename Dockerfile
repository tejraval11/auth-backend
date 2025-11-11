FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
RUN chmod +x mvnw
EXPOSE 8080
CMD ["java", "-jar", "target/*.jar"]
