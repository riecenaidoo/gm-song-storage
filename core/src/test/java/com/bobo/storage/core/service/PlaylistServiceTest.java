package com.bobo.storage.core.service;

import com.bobo.storage.IntegrationTest;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistMother;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import com.bobo.storage.core.resource.access.SongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Set;

@IntegrationTest
class PlaylistServiceTest {

  private final PlaylistService playlistService;

  private final PlaylistRepository playlistRepository;

  private final SongRepository songRepository;

  //  ------ Testing Data ------

  private PlaylistMother playlists;

  @Autowired
  PlaylistServiceTest(PlaylistService playlistService, PlaylistRepository playlistRepository, SongRepository songRepository) {
    this.playlistService = playlistService;
    this.playlistRepository = playlistRepository;
    this.songRepository = songRepository;
  }

  @BeforeEach
  void setUp() {
    this.playlists = new PlaylistMother().withNames().withSongs();
  }

  /**
   * Ensure that when writing this test we are saving <code>new</code> objects.
   * We do not want to be testing updates to a managed entity in the persistence context.
   */
  @Test
  void addingExistingSongsShouldNotCreateDuplicates() {
    // Given
    Playlist playlist = playlists.get();
    Playlist playlistWithSameSongs = playlists.withSongs(playlist::getSongs).get();

    // When
    playlistService.create(playlist);
    playlistService.create(playlistWithSameSongs);

    // Assuming
    Assertions.assertEquals(2, playlistRepository.findAll().size(), "Test Assumption Failed");

    // Then
    Assertions.assertEquals(playlist.getSongs().size(), songRepository.findAll().size());
  }

  @Test
  void updatingSongsInPlaylistWithExistingSongsShouldNotCauseDuplication() {
    // Given
    Playlist playlist = playlists.get();
    playlistService.create(playlist);

    // When
    Set<Song> songs = playlist.getSongs();
    playlistService.addSongs(playlist, new ArrayList<>(songs));

    // Assuming
    Assertions.assertEquals(1, playlistRepository.findAll().size(), "Test Assumption Failed");

    // Then
    Assertions.assertEquals(songs.size(), songRepository.findAll().size());
  }

}