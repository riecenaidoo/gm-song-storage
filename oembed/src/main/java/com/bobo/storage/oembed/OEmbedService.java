package com.bobo.storage.oembed;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.SongService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collection;

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
      song.setLastLookup(LocalDateTime.now());
      Provider.lookupSong(song, webClient);
      service.updateSong(song);
    }

  }

}
