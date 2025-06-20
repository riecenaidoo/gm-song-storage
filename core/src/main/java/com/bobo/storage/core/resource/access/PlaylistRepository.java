package com.bobo.storage.core.resource.access;

import com.bobo.storage.core.domain.Playlist;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends AccessRepository<Playlist, Integer> {}
