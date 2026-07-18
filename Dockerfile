FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/task-manager-*.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]