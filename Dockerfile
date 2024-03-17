FROM container-registry.oracle.com/java/jdk:21.0.2-oraclelinux8
ARG APP_JAR_PATH=./oauth2-authorization-server/build/libs/oauth2-authorization-server.jar
ARG JKS_CERT_PATH=./oauth2-authorization-server/src/main/resources/jankbyte-sso.jks
ARG IMPORTCERT_SH_SCRIPT_PATH=./importcert.sh
ARG WORKDIR_PATH=/app
ENV spring.profiles.active=production
ENV DATASOURCE_URL=
ENV DATASOURCE_USERNAME=
ENV DATASOURCE_PASSWORD=
ENV OAUTH2_CLIENT_ID=
ENV OAUTH2_CLIENT_SECRET=
ENV REDIS_HOST=
ENV REDIS_PORT=
ENV REDIS_PASSWORD=
ENV BASE_URL=
ENV JKS_PASSWORD=
WORKDIR ${WORKDIR_PATH}
COPY ${APP_JAR_PATH} app.jar
COPY ${JKS_CERT_PATH} cert.jks
COPY ${IMPORTCERT_SH_SCRIPT_PATH} importcert.sh
USER root
RUN /bin/bash importcert.sh "cert.jks" "${KEY_STORE_PASSWORD}" "changeit" "jankbyte-sso"
CMD java -DbaseUrl=${BASE_URL} \
    -Doauth2.resourceserver.opaque.client-secret=${OAUTH2_CLIENT_SECRET} \
    -Doauth2.resourceserver.opaque.client-id=${OAUTH2_CLIENT_ID} \
    -Dredis.host=${REDIS_HOST} \
    -Dredis.port=${REDIS_PORT} \
    -Dredis.password=${REDIS_PASSWORD} \
    -Ddatasource.url=${DATASOURCE_URL} \
    -Ddatasource.username=${DATASOURCE_USERNAME} \
    -Ddatasource.password=${DATASOURCE_PASSWORD} \
    -Dssl.jks.password=${JKS_PASSWORD} \
    -Duser.timezone=UTC \
    -Duser.language=en_US \
    -jar app.jar