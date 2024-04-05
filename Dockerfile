FROM gradle:8.4-focal AS build
WORKDIR /workspace

COPY src ./src
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle
COPY gradle.properties ./gradle.properties

RUN gradle clean build

FROM bellsoft/liberica-openjdk-debian:17

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app

COPY --from=build /workspace/build/libs/dl-manager-0.0.1.jar ./application.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "application.jar"]