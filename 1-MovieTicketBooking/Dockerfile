# ============================
# 1) Build Stage
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests for speed; remove -DskipTests to run tests)
RUN mvn clean package -DskipTests

# ============================
# 2) Runtime Stage
# ============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the produced jar (adjust the pattern if needed)
# If your final jar is TIcketNest.jar, you can copy and rename it to app.jar:
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]