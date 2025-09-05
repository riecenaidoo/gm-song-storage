package com.bobo.storage.core.service;

import static org.mockito.Mockito.mock;

import com.bobo.semantic.UnitTest;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.PlaylistSongMother;
import com.bobo.storage.core.resource.PlaylistSongRepository;
import java.util.Random;
import org.junit.jupiter.api.*;

@UnitTest(PlaylistSongServiceImpl.class)
class PlaylistSongServiceImplTest {

	// Test Utilities

	private final Random random = new Random();

	// Test Targets

	private PlaylistSongService service;

	@BeforeEach
	void setUp() {
		PlaylistSongRepository repository = mock(PlaylistSongRepository.class);
		service = new PlaylistSongServiceImpl(repository, mock(SongService.class));
	}

	/**
	 * @see PlaylistSongServiceImpl#add(PlaylistSong)
	 */
	@Nested
	class Create {

		@DisplayName("Throw IllegalArgumentException if song has a TechnicalID assigned.")
		@Test
		void songHasATechnicalID() {
			// Given
			PlaylistSong song = new PlaylistSongMother(random).withIds().get();

			// Then
			Assertions.assertThrows(
					IllegalArgumentException.class,
					// When
					() -> service.add(song));
		}
	}
}
