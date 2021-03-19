FROM openjdk:11-jdk-slim
WORKDIR /tmp
COPY . .
RUN ./gradlew --console=plain --warning-mode all clean build -x check -x test

FROM openjdk:11-jre-slim
EXPOSE 8080
WORKDIR /srv
COPY --from=0 /tmp/application/build/libs/app.jar .
CMD java -jar app.jar
