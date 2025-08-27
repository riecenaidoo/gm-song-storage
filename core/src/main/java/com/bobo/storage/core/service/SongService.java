package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.semantic.CreateResource;
import java.util.Collection;
import java.util.Optional;

/**
 * {@code Song} management is an internal {@code Service} that is automated by the system.
 *
 * <p>The public facing API is limited.
 */
public interface SongService extends CreateResource<Song> {

	Optional<Song> findById(int id);

	Optional<Song> findByUrl(String url);

	/**
	 * @return a {@link Collection} of all {@link Song} resources that are eligible for a lookup.
	 * @see Song#lookedUp()
	 * @see SongLookupService
	 * @apiNote In future, we will accept a {@code limit} parameter to control the return size.
	 */
	Collection<Song> getLookupCandidates();

	/** TODO define {@code UpdateResource<R>}. */
	Song updateSong(Song song);

	void delete(Song song);
}
