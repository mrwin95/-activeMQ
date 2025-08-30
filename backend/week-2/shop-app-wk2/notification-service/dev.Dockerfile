# build stage

FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /build
# Copy full project
COPY pom.xml ./pom.xml
COPY . ./

# Build ONLY notification-service (correct flag: -DskipTests)
RUN mvn -q -DskipTests -f notification-service/pom.xml clean package

# Run stage
FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /build/notification-service/target/*.jar /app/app.jar

EXPOSE 9981
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

