package com.bobo.storage.core.service.impl;

import com.bobo.semantic.UnitTest;
import com.bobo.storage.core.domain.EntityMother;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.domain.SongMother;
import com.bobo.storage.core.resource.access.SongRepository;
import com.bobo.storage.core.service.SongService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest(SongServiceImpl.class)
class SongServiceImplTest {

  private SongRepository repository;

  // Test Utilities

  private final Random random = new Random();

  // Test Targets

  private SongService service;

  @BeforeEach
  void setUp() {
    repository = mock(SongRepository.class);
    service = new SongServiceImpl(repository);
  }

  /**
   * @see SongServiceImpl#create(Song)
   */
  @Nested
  class Create {

    @Test
    void createSong() {
      // Given
      Song song = new SongMother(random).withUrls().get();

      // Stubbing
      when(repository.findByUrl(song.getUrl())).thenReturn(Optional.empty());
      when(repository.save(song)).then((ans) -> EntityMother.setId(song, 1));

      // When
      Song createdSong = service.create(song);

      // Then
      Assertions.assertEquals(song, createdSong);
      verify(repository, times(1)).save(song);
    }

    @DisplayName("Throw IllegalArgumentException if song has a TechnicalID assigned.")
    @Test
    void songHasATechnicalID() {
      // Given
      Song song = new SongMother(random).withIds().withUrls().get();

      // Then
      Assertions.assertThrows(IllegalArgumentException.class,
                              // When
                              () -> service.create(song));
    }

    @DisplayName("Do not save another Song, if an entry for it already exists.")
    @Test
    void songAlreadyExists() {
      SongMother mother = new SongMother(random).withUrls();
      // Given
      Song song = mother.get();
      Song existingEntity = mother.withUrls(song::getUrl).withIds().get();

      // Stubbing
      when(repository.findByUrl(song.getUrl())).thenReturn(Optional.of(existingEntity));

      // When
      Song createdSong = service.create(song);

      // Then
      Assertions.assertEquals(song, createdSong, "Returned Song must be equivalent to Song argument.");
      Assertions.assertSame(existingEntity, createdSong, "Should return the managed entity for further use.");
      verify(repository, times(0)).save(song);
    }

  }

}