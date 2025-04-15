package com.bobo.storage.core.resource.access;

import com.bobo.storage.IntegrationTest;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.domain.SongMother;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Random;

/**
 * These tests are specifically for repository methods I define using the query language.
 * I do not need to test the supplied methods like {@code findById}, etc.
 * I just need to confirm the query methods I define work as I expect.
 * <p>
 * They probably would only be necessary to re-run if I change the domain model, otherwise they can be skipped.
 * TODO Figure out if I could define a test suite?
 * <p>
 * TODO [housekeeping] revise {@link IntegrationTest} annotation.
 * <p>
 * This is an {@code IntegrationTest} but I can't use my annotation because tried to compose {@link IntegrationTest},
 * a better idea would be to just use the {@code Test} annotations to document expectations for tests, conventions,
 * standards, etc.
 */
@DataJpaTest
class SongRepositoryIT {

  // Test Utilities

  private final Random random = new Random();

  // Test Targets

  private final SongRepository repository;

  @Autowired
  SongRepositoryIT(SongRepository repository) {
    this.repository = repository;
  }

  @Test
  void findByUrl() {
    // Given
    Song song = new SongMother(random).withUrls().get();
    song = repository.save(song);
    Assertions.assertTrue(repository.findById(song.getId()).isPresent(), "Test assumption failed.");

    // When
    Song retrievedSong = repository.findByUrl(song.getUrl()).orElseThrow(AssertionError::new);

    // Then
    Assertions.assertEquals(song, retrievedSong);
  }

}