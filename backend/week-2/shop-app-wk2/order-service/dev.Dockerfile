# build stage

FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /build
# Copy full project
COPY pom.xml ./pom.xml
COPY . ./

# Build ONLY notification-service (correct flag: -DskipTests)
RUN mvn -q -DskipTests -f pom.xml -pl :order-service -am clean package

# Run stage
FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /build/order-service/target/*.jar /app/app.jar

EXPOSE 9988
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

