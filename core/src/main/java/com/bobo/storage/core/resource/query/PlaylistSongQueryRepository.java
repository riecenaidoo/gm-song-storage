package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface PlaylistSongQueryRepository extends QueryRepository<PlaylistSong, Integer> {

  @Override
  default Class<PlaylistSong> resource() {
    return PlaylistSong.class;
  }

  Stream<PlaylistSong> findAllByPlaylist(Playlist playlist);

}
