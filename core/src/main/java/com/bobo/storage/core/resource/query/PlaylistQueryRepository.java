package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import java.util.Collection;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistQueryRepository extends QueryRepository<Playlist, Integer> {

	Collection<Playlist> findAll();

	/**
	 * @param nameFragment fragment of a {@link Playlist} {@code name} to search for. Neither {@code
	 *     null}, nor blank ({@link String#isBlank()}).
	 * @return {@code Collection} containing zero or more matching {@code Playlists}.
	 */
	Collection<Playlist> findAllByNameContainingIgnoringCase(String nameFragment);
}
