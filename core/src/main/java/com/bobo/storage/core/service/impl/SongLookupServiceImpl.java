package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Provider;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import com.bobo.storage.core.service.SongLookupService;
import com.bobo.storage.core.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SongLookupServiceImpl implements SongLookupService {

  private static final Logger log = LoggerFactory.getLogger(SongLookupServiceImpl.class);

  private final WebClient webClient;

  private final SongQueryRepository songs;

  private final SongService songService;

  private final PlaylistSongService playlistSongService;

  public SongLookupServiceImpl(WebClient webClient, SongQueryRepository songs, SongService songService,
                               PlaylistSongService playlistSongService) {
    this.webClient = webClient;
    this.songs = songs;
    this.songService = songService;
    this.playlistSongService = playlistSongService;
  }


  /**
   * This is the atomic operation. If it fails the batch should not fail, but this individual should be retried.
   * <p>In the event of a redirection, we defer lookup for the next pass. It is very possible to be redirected
   * multiple times. We should only poll Providers on a stable URL.</p>
   */
  @Transactional
  public void lookup(Song song) {
    if (song.verifyUrl(webClient)) {
      Optional<Song> existingSong = songs.findByUrl(song.getUrl());
      if (existingSong.isPresent() && !existingSong.get().getId().equals(song.getId())) {
        log.info("Lookup#Redirection: Song(id:{}) Redirects to existing Song(id:{}), will migrate to existing Song.",
                 song.getId(),
                 existingSong.get().getId());
        playlistSongService.migrate(song, existingSong.get());
        songService.delete(song);
      } else {
        log.debug("Lookup#Redirection: Song(id:{}) redirects. URL updated. Lookup deferred.",
                  song.getId());
        songService.updateSong(song);
      }
      return;
    }

    Provider.lookupSong(song, webClient); // TODO [design] Returns Optional<Provider> for later co-ordination?
    song.setLastLookup(LocalDateTime.now());
    songService.updateSong(song);
  }

}
