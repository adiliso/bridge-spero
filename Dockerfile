FROM docker.io/library/gradle:jdk21-alpine AS builder
WORKDIR /usr/app
COPY . .
RUN gradle build

FROM docker.io/eclipse-temurin:21-jre-alpine AS runner
LABEL org.opencontainers.image.source=https://github.com/adiliso/bridge-spero
RUN addgroup -S spring -g 111 && adduser -S spring -G spring -u 111
USER spring:spring
COPY --from=builder --chown=spring:spring /usr/app/build/libs/*.jar server.jar
ENTRYPOINT ["java","-jar","/server.jar"]
