FROM openjdk:17-jdk-slim
COPY --chmod=777 build/libs/demo-single-version.jar /application/demo.jar
CMD ["java", "-jar", "/application/demo.jar"]