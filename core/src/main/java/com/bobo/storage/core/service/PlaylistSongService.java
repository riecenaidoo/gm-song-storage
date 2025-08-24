package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.semantic.CreateResource;
import java.util.Collection;
import java.util.Optional;

public interface PlaylistSongService extends CreateResource<PlaylistSong> {

	void delete(PlaylistSong song);

	/**
	 * Migrate the {@code Song} associated with a {@code PlaylistSong}.
	 *
	 * <p>This is typically done to facilitate merging {@code Songs} that have been discovered as
	 * identical, or when the original {@code Provider} can no longer host the {@code Song}, but
	 * another {@code Provider} does.
	 *
	 * @param from {@code Song} to transfer from.
	 * @param to {@code Song} to transfer to.
	 */
	void migrate(Song from, Song to);

	Optional<PlaylistSong> findById(int id);

	Collection<PlaylistSong> findAllByPlaylist(Playlist playlist);
}
