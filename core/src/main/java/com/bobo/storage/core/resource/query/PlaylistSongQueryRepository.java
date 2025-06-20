package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongQueryRepository extends QueryRepository<PlaylistSong, Integer> {

	List<PlaylistSong> findAllByPlaylist(Playlist playlist);

	List<PlaylistSong> findAllBySong(Song song);
}
