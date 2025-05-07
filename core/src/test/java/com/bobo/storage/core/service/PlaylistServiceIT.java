package com.bobo.storage.core.service;

import com.bobo.storage.TestConfig;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistMother;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.domain.SongMother;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import com.bobo.storage.core.resource.access.SongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Marked for removal.
 * <p>
 * {@link PlaylistService} is undergoing changes for v2. These tests are deprecated.
 */
@Transactional
@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
@Deprecated
class PlaylistServiceIT {

  private final PlaylistService service;

  private final PlaylistRepository repository;

  private final SongRepository songRepository;

  //  ------ Testing Data ------

  private final Random random = new Random();

  @Autowired
  PlaylistServiceIT(PlaylistService service, PlaylistRepository repository, SongRepository songRepository) {
    this.service = service;
    this.repository = repository;
    this.songRepository = songRepository;
  }

  /**
   * Ensure that when writing this test we are saving <code>new</code> objects.
   * We do not want to be testing updates to a managed entity in the persistence context.
   */
  @Test
  void addingExistingSongsShouldNotCreateDuplicates() {
    // Given
    final int numSongs = random.nextInt(1, 10);
    final int numPlaylists = random.nextInt(1, 10);
    final Collection<Song> songs = new SongMother(random).get(numSongs).collect(Collectors.toSet());

    Collection<Playlist> playlists = new PlaylistMother(random).withSongs(() -> songs).get(numPlaylists).toList();

    // When
    playlists.forEach(service::create);

    // Assuming
    Assertions.assertEquals(numPlaylists, repository.findAll().size(), "Test Assumption Failed");

    // Then
    Assertions.assertEquals(numSongs, songRepository.findAll().size());
  }

  @Test
  void updatingSongsInPlaylistWithExistingSongsShouldNotCauseDuplication() {
    // Given
    Playlist playlist = new PlaylistMother(random).withSongs().get();
    service.create(playlist);

    // When
    Set<Song> songs = playlist.getSongs();
    service.addSongs(playlist, new ArrayList<>(songs));

    // Assuming
    Assertions.assertEquals(1, repository.findAll().size(), "Test Assumption Failed");

    // Then
    Assertions.assertEquals(songs.size(), songRepository.findAll().size());
  }

}