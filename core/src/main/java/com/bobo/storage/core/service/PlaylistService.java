package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.semantic.Create;
import com.bobo.storage.core.semantic.Read;
import java.util.Collection;

public interface PlaylistService extends Create<Playlist>, Read<Playlist> {

	/**
	 * @param name a case-insensitive {@code name}, or part thereof, to search for.
	 * @return a {@link Collection} of {@link Playlist} resources that match, or contain, the queried
	 *     {@code name}; never null.
	 * @apiNote In future, this method will support flags to control case sensitivity, word matching,
	 *     prefix matching, and other search behaviors.
	 * @implSpec {@link Read#get()}
	 */
	Collection<Playlist> searchByName(String name);

	Playlist update(Playlist playlist);

	void delete(Playlist playlist);
}
