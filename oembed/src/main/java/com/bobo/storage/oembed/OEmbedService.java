package com.bobo.storage.oembed;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.SongService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Polls {@code oEmbed} {@code Providers} for metadata about {@code Songs}.
 * <p>
 * There is no {@code interface} as this service is not driven by a public API,
 * it drives itself through {@link Scheduled} jobs.
 */
@Service
public class OEmbedService {

  private final WebClient webClient;

  private final SongService service;

  private final SongQueryRepository songs;

  public OEmbedService(WebClient webClient, SongService service, SongQueryRepository songs) {
    this.webClient = webClient;
    this.service = service;
    this.songs = songs;
  }

  /**
   * Look-up metadata for {@code Songs} that were recently created.
   * <p>
   * TODO add logging for observability into the scheduled Job.
   */
  @Scheduled(cron = "0 * * * * *")
  public void lookupNewSongs() {
    Collection<Song> songs = this.songs.findAllByLastLookupIsNull();
    if (songs.isEmpty()) {
      return;
    }

    for (Song song : songs) {
      Optional<OEmbedResponse> metadata = lookupSong(song);
      if (metadata.isPresent()) {
        metadata.get().title().ifPresent(song::setTitle);
        metadata.get().authorName().ifPresent(song::setArtist);
        metadata.get().thumbnailUrl().ifPresent(song::setThumbnailUrl);
      }
      song.setLastLookup(LocalDateTime.now());
      service.updateSong(song);
    }
  }

  /**
   * Poll {@code Providers} that may host the {@code url} of the {@link Song}.
   *
   * @param song to look up.
   * @return metadata about a {@code Song} from an {@code oEmbed} {@link Provider},
   * if one could be found.
   */
  public Optional<OEmbedResponse> lookupSong(Song song) {

    URL url;
    try {
      url = URI.create(song.getUrl()).toURL();
    } catch (MalformedURLException e) {
      // TODO [design] the Song URL may be stored as a String, but it should probably be typed to a URL.
      throw new RuntimeException("The URL of the Song was invalid.");
    }

    for (Provider provider : Provider.values()) {
      URL query = provider.getQuery(url);
      URI queryURI;
      try {
        queryURI = query.toURI();
      } catch (URISyntaxException e) {
        // TODO [design] Should probably question whether #getQuery should return a URL or URI then.
        throw new RuntimeException();
      }

      Optional<OEmbedResponse> metadata = webClient.get().uri(queryURI)
                                                   .accept(MediaType.APPLICATION_JSON)
                                                   .exchangeToMono(OEmbedService::toMono)
                                                   .blockOptional();

      if (metadata.isPresent()) {
        return metadata;
      }
    }

    return Optional.empty();
  }

  /**
   * Extracted for method referencing.
   *
   * @param response from an oEmbed API.
   * @return a {@link Mono} of an {@link OEmbedResponse},
   * if the API responded with {@code 200 OK} otherwise {@link Mono#empty()}.
   */
  private static Mono<OEmbedResponse> toMono(ClientResponse response) {
    return (response.statusCode().equals(HttpStatus.OK)) ? response.bodyToMono(OEmbedResponse.class) : Mono.empty();
  }

}
