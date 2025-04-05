## Make File

A phony rule to initialise the repository. Things like downloading documentation, etc.

```
mvn dependency::sources
```

A phony rule to handle rebuilding the local image for developing.

```
mvn clean package -Dmaven.test.skip=true
```

```
docker compose up -d --build
```

## Dockerfile

The `Dockerfile` should be multi-stage, it should be able to build the package using the `mvn` image,
then include only the artifact in the final image. I tried this before, but something failed with the Spring app packaging,
something about a final step that happens after it is packaged.

The other issue was figuring out how to mount a volume in the first stage for mvn.

- https://hub.docker.com/_/maven
- https://docs.docker.com/build/building/multi-stage/
- https://docs.spring.io/spring-boot/maven-plugin/build-image.html