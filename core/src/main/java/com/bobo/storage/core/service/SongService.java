package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.semantic.Create;
import com.bobo.storage.core.semantic.Read;
import java.util.Collection;
import java.util.Optional;

/**
 * {@code Song} management is an internal {@code Service} that is automated by the system.
 *
 * <p>The public facing API is limited.
 */
public interface SongService extends Create<Song>, Read<Song> {

	/**
	 * Find a {@link Song} by its {@code url}, which uniquely identifies it.
	 *
	 * @param url the unique reference {@code url} of the {@link Song}.
	 * @return the {@link Song} if found, otherwise Optional.empty().
	 * @implSpec {@link Read#find(int)}
	 */
	Optional<Song> findByUrl(String url);

	/**
	 * @return a {@link Collection} of all {@link Song} resources that are eligible for a lookup;
	 *     never null.
	 * @see Song#lookedUp()
	 * @see SongLookupService
	 * @apiNote In future, we will accept a {@code limit} parameter to control the return size.
	 * @implSpec {@link Read#get()}
	 */
	Collection<Song> getLookupCandidates();

	/** TODO define {@code UpdateResource<R>}. */
	Song updateSong(Song song);

	void delete(Song song);
}
