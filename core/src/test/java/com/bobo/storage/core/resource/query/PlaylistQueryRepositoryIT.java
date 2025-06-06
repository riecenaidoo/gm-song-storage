package com.bobo.storage.core.resource.query;

import com.bobo.semantic.IntegrationTest;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistMother;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Random;

@DataJpaTest
@IntegrationTest({PlaylistQueryRepository.class, Repository.class})
class PlaylistQueryRepositoryIT {

  private final PlaylistQueryRepository repository;

  private final PlaylistRepository playlistRepository;

  private final Random random = new Random();

  @Autowired
  PlaylistQueryRepositoryIT(PlaylistQueryRepository repository, PlaylistRepository playlistRepository) {
    this.repository = repository;
    this.playlistRepository = playlistRepository;
  }

  /**
   * Expecting generated query to use {@code WHERE} clause with a {@code LIKE '%...%'},
   * and to wrap operands in {@code upper()/lower()} for case insensitivity.
   *
   * @see PlaylistQueryRepository#findAllByNameContainingIgnoringCase(String)
   */
  @Nested
  class FindAllByNameContainingIgnoringCase {

    @Test
    void byExactName() {
      // Given
      Playlist playlist = new PlaylistMother(random).withNames().get();
      playlist = playlistRepository.save(playlist);
      Assertions.assertTrue(repository.findById(playlist.getId()).isPresent(), "Test assumption failed.");

      // When
      Collection<Playlist> playlists = repository.findAllByNameContainingIgnoringCase(playlist.getName());

      // Then
      Assertions.assertTrue(playlists.contains(playlist));
    }

    /**
     * Repeat {@link #byExactName()} exactly, but set arg to {@code toLower}.
     */
    @Test
    void byExactNameIgnoringCase() {
      // Given
      Playlist playlist = new PlaylistMother(random).withNames().get();
      playlist = playlistRepository.save(playlist);
      Assertions.assertTrue(repository.findById(playlist.getId()).isPresent(), "Test assumption failed.");

      // When
      Collection<Playlist> playlists = repository.findAllByNameContainingIgnoringCase(playlist.getName().toLowerCase());

      // Then
      Assertions.assertTrue(playlists.contains(playlist));
    }

    @Test
    void byPartialName() {
      // Given
      Playlist playlist = new PlaylistMother(random).withNames(() -> "Some Long Name").get();
      playlist = playlistRepository.save(playlist);
      Assertions.assertTrue(repository.findById(playlist.getId()).isPresent(), "Test assumption failed.");

      // When
      for (String partialName : playlist.getName().split(" ")) {
        Collection<Playlist> playlists = repository.findAllByNameContainingIgnoringCase(partialName);

        // Then
        Assertions.assertTrue(playlists.contains(playlist),
                              "Partial: %s should match, but doesn't.".formatted(partialName));
      }
    }

    /**
     * Repeat {@link #byPartialName()} exactly, but set arg to {@code toLower}.
     */
    @Test
    void byPartialNameIgnoringCase() {
      // Given
      Playlist playlist = new PlaylistMother(random).withNames(() -> "Some Long Name").get();
      playlist = playlistRepository.save(playlist);
      Assertions.assertTrue(repository.findById(playlist.getId()).isPresent(), "Test assumption failed.");

      // When
      for (String partialName : playlist.getName().split(" ")) {
        partialName = partialName.toLowerCase();
        Collection<Playlist> playlists = repository.findAllByNameContainingIgnoringCase(partialName);

        // Then
        Assertions.assertTrue(playlists.contains(playlist),
                              "Partial: %s should match, but doesn't.".formatted(partialName));
      }
    }

    /**
     * Sanity test that the query doesn't bring back everything.
     */
    @Test
    void onlyIfMatch() {
      // Given
      Playlist playlist = new PlaylistMother(random).withNames(() -> "Some Name").get();
      playlist = playlistRepository.save(playlist);
      Assertions.assertTrue(repository.findById(playlist.getId()).isPresent(), "Test assumption failed.");

      // When
      String argumentThatShouldNotMatch = "Other";
      Assertions.assertNotEquals(argumentThatShouldNotMatch, playlist.getName(), "Test assumption failed.");
      Assertions.assertFalse(playlist.getName().contains(argumentThatShouldNotMatch), "Test assumption failed.");

      Collection<Playlist> playlists = repository.findAllByNameContainingIgnoringCase(argumentThatShouldNotMatch);

      // Then
      Assertions.assertFalse(playlists.contains(playlist));
      Assertions.assertTrue(playlists.isEmpty());
    }

    /**
     * Confirm that there are no false positives when there are {@code Playlists} with similar names,
     * but the argument is the only found in the one.
     */
    @Test
    void similarNameButArgumentDifferent() {
      // Given
      PlaylistMother mother = new PlaylistMother(random).withNames(() -> "Similar Name");
      Playlist similarPlaylist = mother.get();
      Playlist playlist = mother.get();
      String differenceBetweenThem = " Delta";
      playlist.setName(playlist.getName() + differenceBetweenThem);
      playlistRepository.save(similarPlaylist);
      playlistRepository.save(playlist);
      Assertions.assertTrue(repository.findById(playlist.getId()).isPresent(), "Test assumption failed.");
      Assertions.assertTrue(repository.findById(similarPlaylist.getId()).isPresent(), "Test assumption failed.");

      // When
      Collection<Playlist> playlists = repository.findAllByNameContainingIgnoringCase(differenceBetweenThem);

      // Then
      Assertions.assertFalse(playlists.contains(similarPlaylist));
      Assertions.assertTrue(playlists.contains(playlist));
    }

    @Test
    void emptyCollectionIfNoMatch() {
      // Given
      Collection<Playlist> playlists = new PlaylistMother(random).withNames(() -> "Some Name").get(5).toList();
      playlists = playlistRepository.saveAll(playlists);
      for (Playlist playlist : playlists) {
        Assertions.assertTrue(repository.findById(playlist.getId()).isPresent(), "Test assumption failed.");
      }

      // When
      String argumentThatShouldNotMatch = "Other";
      for (Playlist playlist : playlists) {
        Assertions.assertNotEquals(argumentThatShouldNotMatch, playlist.getName(), "Test assumption failed.");
        Assertions.assertFalse(playlist.getName().contains(argumentThatShouldNotMatch), "Test assumption failed.");
      }

      Collection<Playlist> queryResult = repository.findAllByNameContainingIgnoringCase(argumentThatShouldNotMatch);

      // Then
      Assertions.assertTrue(queryResult.isEmpty());
    }

  }

}