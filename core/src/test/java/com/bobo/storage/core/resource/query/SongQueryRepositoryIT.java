package com.bobo.storage.core.resource.query;

import com.bobo.semantic.IntegrationTest;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.domain.SongMother;
import com.bobo.storage.core.resource.access.SongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.Repository;

import java.util.Random;

/**
 * These tests are specifically for repository methods I define using the query language.
 * I do not need to test the supplied methods like {@code findById}, etc.
 * I just need to confirm the query methods I define work as I expect.
 * <p>
 * They probably would only be necessary to re-run if I change the domain model, otherwise they can be skipped.
 * TODO Figure out if I could define a test suite?
 */
@DataJpaTest
@IntegrationTest({SongQueryRepository.class, Repository.class})
class SongQueryRepositoryIT {

  // Test Utilities

  private final Random random = new Random();

  private final SongRepository songRepository;

  // Test Targets

  private final SongQueryRepository repository;

  @Autowired
  SongQueryRepositoryIT(SongRepository songRepository, SongQueryRepository repository) {
    this.songRepository = songRepository;
    this.repository = repository;
  }

  @Test
  void findByUrl() {
    // Given
    Song song = new SongMother(random).withUrls().get();
    song = songRepository.save(song);
    Assertions.assertTrue(repository.findById(song.getId()).isPresent(), "Test assumption failed.");

    // When
    Song retrievedSong = repository.findByUrl(song.getUrl()).orElseThrow(AssertionError::new);

    // Then
    Assertions.assertEquals(song, retrievedSong);
  }

}