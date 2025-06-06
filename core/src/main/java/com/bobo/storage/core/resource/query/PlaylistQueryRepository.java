package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Stream;

@Repository
public interface PlaylistQueryRepository extends QueryRepository<Playlist, Integer> {

  Collection<Playlist> findAll();

  /**
   * @param nameFragment fragment of a {@link Playlist} {@code name} to search for.
   *                     Neither {@code null}, nor blank ({@link String#isBlank()}).
   * @return {@code Collection} containing zero or more matching {@code Playlists}.
   */
  Collection<Playlist> findAllByNameContainingIgnoringCase(String nameFragment);

}
