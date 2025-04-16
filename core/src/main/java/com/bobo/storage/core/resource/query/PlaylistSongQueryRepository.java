package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PlaylistSongQueryRepository extends QueryRepository<PlaylistSong, Integer> {

  @Override
  default Class<PlaylistSong> resource() {
    return PlaylistSong.class;
  }

  /**
   * TODO migrate to List when order or sorting is introduced.
   */
  Collection<PlaylistSong> findAllByPlaylist(Playlist playlist);

}
