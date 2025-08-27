package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.semantic.CreateResource;
import java.util.Collection;
import java.util.Optional;

public interface PlaylistService extends CreateResource<Playlist> {

	Optional<Playlist> findById(int id);

	Collection<Playlist> findAll();

	/**
	 * @param name a case-insensitive {@code name}, or part thereof, to search for.
	 * @return a {@link Collection} of {@link Playlist} resources that match, or contain, the queried
	 *     {@code name}.
	 * @apiNote In future, this method will support flags to control case sensitivity, word matching,
	 *     prefix matching, and other search behaviors.
	 */
	Collection<Playlist> searchByName(String name);

	Playlist update(Playlist playlist);

	void delete(Playlist playlist);
}
