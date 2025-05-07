package com.bobo.storage.core.resource.query;

import com.bobo.semantic.IntegrationTest;
import com.bobo.storage.core.domain.*;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import com.bobo.storage.core.resource.access.PlaylistSongRepository;
import com.bobo.storage.core.resource.access.SongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Random;

@DataJpaTest
@IntegrationTest({PlaylistSongQueryRepository.class, Repository.class})
class PlaylistSongQueryRepositoryIT {

  // Test Utilities

  private final Random random = new Random();

  private final PlaylistSongRepository accessRepository;

  private final PlaylistRepository playlistRepository;

  private final SongRepository songRepository;

  // Test Targets

  private final PlaylistSongQueryRepository queryRepository;

  @Autowired
  PlaylistSongQueryRepositoryIT(PlaylistSongRepository accessRepository,
                                PlaylistRepository playlistRepository,
                                SongRepository songRepository,
                                PlaylistSongQueryRepository queryRepository) {
    this.accessRepository = accessRepository;
    this.playlistRepository = playlistRepository;
    this.songRepository = songRepository;
    this.queryRepository = queryRepository;
  }

  /**
   * @see PlaylistSongQueryRepository#findAllByPlaylist(Playlist)
   */
  @Test
  void findAllByPlaylist() {
    // Given
    Playlist playlist = playlistRepository.save(new PlaylistMother(random).get());
    Song song = songRepository.save(new SongMother(random).get());

    accessRepository.save(new PlaylistSong(playlist, song));

    // When
    Collection<PlaylistSong> playlistSongs = queryRepository.findAllByPlaylist(playlist).toList();

    // Then
    Assertions.assertEquals(1, playlistSongs.size());
    for (PlaylistSong playlistSong : playlistSongs) {
      Assertions.assertSame(playlist, playlistSong.getPlaylist());
    }
  }

}