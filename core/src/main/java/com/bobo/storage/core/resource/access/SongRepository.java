package com.bobo.storage.core.resource.access;

import com.bobo.storage.core.domain.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends AccessRepository<Song, Integer> {

}
