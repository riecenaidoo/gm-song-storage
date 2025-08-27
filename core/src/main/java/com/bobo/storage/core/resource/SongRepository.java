package com.bobo.storage.core.resource;

import com.bobo.storage.core.domain.Song;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @implSpec {@link EntityRepository}
 */
@Repository
public interface SongRepository
		extends EntityRepository<Song, Integer>, CrudRepository<Song, Integer> {

	Optional<Song> findByUrl(String url);

	Collection<Song> findAllByLastLookupIsNull();
}
