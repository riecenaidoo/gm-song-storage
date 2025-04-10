
```
mvn clean package
docker compose up -d --build
```

```
docker-compose logs db
```

```
docker-compose logs api
```

## Connecting to the Database

```
psql -h localhost -p 5050 -U postgres -d gm_song_storage
```

