package com.bobo.storage.core.playlist.song;

import com.bobo.semantic.IntegrationTest;
import com.bobo.storage.core.playlist.Playlist;
import com.bobo.storage.core.playlist.PlaylistMother;
import com.bobo.storage.core.playlist.PlaylistTestRepository;
import com.bobo.storage.core.song.Song;
import com.bobo.storage.core.song.SongMother;
import com.bobo.storage.core.song.SongTestRepository;
import java.util.Collection;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.Repository;

@DataJpaTest
@IntegrationTest({PlaylistSongRepository.class, Repository.class})
class PlaylistSongRepositoryIT {

	// Test Utilities

	private final Random random = new Random();

	private final PlaylistTestRepository playlists;

	private final SongTestRepository songs;

	// Test Targets

	private final PlaylistSongRepository repository;

	@Autowired
	PlaylistSongRepositoryIT(
			PlaylistSongRepository repository,
			PlaylistTestRepository playlists,
			SongTestRepository songs) {
		this.repository = repository;
		this.playlists = playlists;
		this.songs = songs;
	}

	/**
	 * @see PlaylistSongRepository#findAllByPlaylist(Playlist)
	 */
	@Test
	void findAllByPlaylist() {
		// Given
		Playlist playlist = playlists.save(new PlaylistMother(random).get());
		Song song = songs.save(new SongMother(random).get());

		repository.save(new PlaylistSong(playlist, song));

		// When
		Collection<PlaylistSong> playlistSongs = repository.findAllByPlaylist(playlist);

		// Then
		Assertions.assertEquals(1, playlistSongs.size());
		for (PlaylistSong playlistSong : playlistSongs) {
			Assertions.assertSame(playlist, playlistSong.getPlaylist());
		}
	}
}
