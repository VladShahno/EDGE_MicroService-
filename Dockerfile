FROM maven:3.6.3-jdk-11-slim as build
WORKDIR /home/app
COPY src src
COPY pom.xml .
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/*.jar /usr/app/EDGE.jar
EXPOSE 8081
CMD java -jar -DskipTests /usr/app/EDGE.jar
