FROM openjdk:17-jdk-slim as app-builder
ARG BUILD_CACHE_PATH
ARG BUILD_COMMAND_ARGS
ENV GRADLE_USER_HOME="$BUILD_CACHE_PATH"
WORKDIR /tmp
COPY . .
RUN ./gradlew --no-daemon "${BUILD_COMMAND_ARGS:-build}"

FROM eclipse-temurin:17-jre
ENV JAVA_OPTS_DOCKER=""
ENV JAVA_MEM_OPTS_DOCKER=""
WORKDIR /srv
COPY --from=app-builder /tmp/application/build/libs/app.jar .
CMD JAVA_OPTS="$JAVA_OPTS $JAVA_OPTS_DOCKER" \
    && JAVA_MEM_OPTS="$JAVA_MEM_OPTS $JAVA_MEM_OPTS_DOCKER" \
    && exec java $JAVA_OPTS $JAVA_MEM_OPTS -jar app.jar
