package com.bobo.storage.core.resource;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import java.util.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @see EntityRepository
 */
@Repository
public interface PlaylistSongRepository
		extends EntityRepository<PlaylistSong, Integer>, CrudRepository<PlaylistSong, Integer> {

	Collection<PlaylistSong> findAllByPlaylist(Playlist playlist);

	Collection<PlaylistSong> findAllBySong(Song song);
}
