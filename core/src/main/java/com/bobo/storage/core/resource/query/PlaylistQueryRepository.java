package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Stream;

@Repository
public interface PlaylistQueryRepository extends QueryRepository<Playlist, Integer> {

  /**
   * Note: This works fine for now because it is a simple select all,
   * but having the return type be a stream actually makes this a streaming query which must be handled differently.
   * <p>
   * Consumption of a streaming query must be bounded by a transaction (read only is fine).
   * <pre>
   *   {@code
   *      @Transactional(readOnly = true)
   *   }
   * </pre>
   *
   * @see InvalidDataAccessApiUsageException
   */
  Stream<Playlist> findAll();

  /**
   * @param nameFragment fragment of a {@link Playlist} {@code name} to search for.
   *                     Neither {@code null}, nor blank ({@link String#isBlank()}).
   * @return {@code Collection} containing zero or more matching {@code Playlists}.
   */
  Collection<Playlist> findAllByNameContainingIgnoringCase(String nameFragment);

}
