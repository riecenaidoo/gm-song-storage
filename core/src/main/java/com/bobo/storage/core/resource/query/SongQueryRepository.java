package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Song;

import java.util.List;
import java.util.Optional;

public interface SongQueryRepository extends QueryRepository<Song, Integer> {


  Optional<Song> findByUrl(String url);

  /**
   * @return newly created {@code Songs} whose {@code url} has never been looked up.
   */
  List<Song> findAllByLastLookupIsNull();

}
