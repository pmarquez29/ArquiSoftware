FROM eclipse-temurin:11-jdk
COPY target/*.jar /app.jar
COPY .env .env
ENV USER_NAME ${POSTGRES_USER}
ENV PASSWORD ${POSTGRES_PASSWORD}
ENV URL ${POSTGRES_URL}

ENTRYPOINT ["java","-jar","/app.jar"]