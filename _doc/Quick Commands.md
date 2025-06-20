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
psql -h localhost -p 5151 -U postgres -d gm_song_storage
```

## Curling

```
curl -X POST localhost:8080/api/v2/playlists \
-H "Content-Type: application/json" \
-d '{"title":"sample"}'
```

```
curl -X POST localhost:8080/api/v2/playlists/1/songs \
-H "Content-Type: application/json" \
-d '{"url":"https://dzr.page.link/QFDvMEXAsJ1buH178"}'
```

