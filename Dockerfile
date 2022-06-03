FROM maven:3.8.5-openjdk-11-slim AS build

COPY src /usr/src/kapyshop-api/src
COPY pom.xml /usr/src/kapyshop-api
RUN mvn -f /usr/src/kapyshop-api/pom.xml clean package

FROM openjdk:11
COPY --from=build /usr/src/kapyshop-api/target/kapyshop-0.0.1-SNAPSHOT.jar /usr/kapyshop-api/kapyshop-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/kapyshop-api/kapyshop-0.0.1-SNAPSHOT.jar"]