FROM adoptopenjdk:11-jre-openj9
ARG JAR_FILE
COPY ./target/messaging-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8081", "-Xms2048M", "-Xmx2560M", "-jar", "/app.jar"]
