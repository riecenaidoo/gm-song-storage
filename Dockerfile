FROM openjdk:17-jdk-alpine

COPY target/*.jar ./gm-song-storage.jar

# [Expose](https://docs.docker.com/reference/dockerfile/#expose)
EXPOSE 8080

CMD ["java", "-jar", "gm-song-storage.jar"]