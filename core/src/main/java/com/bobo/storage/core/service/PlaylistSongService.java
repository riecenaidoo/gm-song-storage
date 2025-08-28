package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.semantic.Create;
import com.bobo.storage.core.semantic.Read;
import java.util.Collection;

public interface PlaylistSongService extends Create<PlaylistSong>, Read<PlaylistSong> {

	/**
	 * @param playlist the source {@link Playlist}.
	 * @return all {@link PlaylistSong} resources within the given {@link Playlist}; never {@code
	 *     null}.
	 * @implSpec {@link Read#get()}
	 */
	Collection<PlaylistSong> getFromPlaylist(Playlist playlist);

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

	void delete(PlaylistSong song);
}
