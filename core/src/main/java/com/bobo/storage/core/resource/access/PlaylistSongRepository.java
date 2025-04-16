package com.bobo.storage.core.resource.access;

import com.bobo.storage.core.domain.PlaylistSong;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongRepository extends AccessRepository<PlaylistSong, Integer> {

}
