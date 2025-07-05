package com.bobo.storage.web.api.v2.response;

import com.bobo.storage.core.domain.Playlist;

public record PlaylistResponse(Integer id, String title) {

	public PlaylistResponse(Playlist playlist) {
		this(playlist.getId(), playlist.getName());
	}
}
