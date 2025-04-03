# Gamemaster Song Storage

## Quick Commands

### Dry Run

```
mvn clean package -Dmaven.test.skip=true
```

```
docker compose up -d --build
```

### Dev

```
mvn dependency::sources
```

```
docker-compose logs db
```

```
docker-compose logs api
```

## TODO

The `Dockerfile` should be multi-stage, it should be able to build the package using the mvn image,
then include only the artifact in the final image. I tried this before, but something failed with the Spring app packaging,
something about a final step that happens after it is packaged.

The other issue was figuring out how to mount a volume in the first stage for mvn.

- https://hub.docker.com/_/maven
- https://docs.docker.com/build/building/multi-stage/
- https://docs.spring.io/spring-boot/maven-plugin/build-image.html
