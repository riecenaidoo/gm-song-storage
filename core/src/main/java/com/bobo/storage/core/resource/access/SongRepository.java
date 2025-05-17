package com.bobo.storage.core.resource.access;

import com.bobo.storage.core.domain.Song;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends AccessRepository<Song, Integer> {

}
