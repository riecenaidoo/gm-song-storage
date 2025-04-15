package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PlaylistQueryRepository extends QueryRepository<Playlist, Integer> {

  @Override
  default Class<Playlist> resource() {
    return Playlist.class;
  }

  Set<Playlist> findAll();

}
