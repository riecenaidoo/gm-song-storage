package com.bobo.storage.web.api.v2.request;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.web.semantic.CreateRequest;

/**
 * A request to create a {@link Song} and within a {@link Playlist}.
 *
 * @param url TODO perhaps make this field {@code song} with the type {@code SongsCreateRequest}, in
 *     the far future, for the sake of consistency with these request objects.
 * @see PlaylistSong#PlaylistSong(Playlist, Song)
 */
public record PlaylistSongsCreateRequest(String url) implements CreateRequest<Song> {

	@Override
	public Song toCreate() {
		return new Song(url);
	}
}
