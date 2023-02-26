FROM eclipse-temurin:11
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]