FROM docker.io/library/gradle:jdk21-alpine AS builder
WORKDIR /usr/app
COPY . .
RUN gradle build

FROM docker.io/eclipse-temurin:21-jre-alpine AS runner
LABEL org.opencontainers.image.source=https://github.com/adiliso/bridge-spero

ARG STATIC_DIR=/usr/share/spring/public
ARG PRIV_DIR=/usr/share/spring/priv

ENV STATIC_DIR=${STATIC_DIR}
ENV PRIV_DIR=${PRIV_DIR}

RUN set -eu; \
    addgroup -S spring -g 111; \
    adduser -S spring -G spring -u 111; \
    mkdir -pv $STATIC_DIR $PRIV_DIR; \
    chown spring:spring $STATIC_DIR $PRIV_DIR

USER spring:spring
COPY --from=builder --chown=spring:spring /usr/app/build/libs/*.jar server.jar

ENTRYPOINT ["java","-jar","/server.jar"]
