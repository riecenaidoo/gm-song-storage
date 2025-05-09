package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistSongQueryRepository extends QueryRepository<PlaylistSong, Integer> {

  List<PlaylistSong> findAllByPlaylist(Playlist playlist);

}
