FROM bitnami/java:21-debian-12

LABEL maintainer="imbrooklyn"

COPY "target/busybox-j-0.0.1-SNAPSHOT.jar" "/app/application.jar"

WORKDIR "/app"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
