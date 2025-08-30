package com.bobo.storage.core.playlist.song;

import static org.mockito.Mockito.mock;

import com.bobo.semantic.UnitTest;
import com.bobo.storage.core.song.SongService;
import java.util.Random;
import org.junit.jupiter.api.*;

@UnitTest(PlaylistSongService.class)
class PlaylistSongServiceTest {

	// Test Utilities

	private final Random random = new Random();

	// Test Targets

	private PlaylistSongService service;

	@BeforeEach
	void setUp() {
		PlaylistSongRepository repository = mock(PlaylistSongRepository.class);
		service = new PlaylistSongService(repository, mock(SongService.class));
	}

	/**
	 * @see PlaylistSongService#add(PlaylistSong)
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
