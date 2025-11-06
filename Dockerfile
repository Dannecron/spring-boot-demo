FROM eclipse-temurin:20-jdk

WORKDIR /app

COPY ./entrypoint.sh .
RUN chmod +x ./entrypoint.sh
COPY ./build/libs/*.jar .

ENTRYPOINT ["/app/entrypoint.sh"]
