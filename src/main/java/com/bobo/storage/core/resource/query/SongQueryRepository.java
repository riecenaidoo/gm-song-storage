package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongQueryRepository extends QueryRepository<Song, String>{

  long count();

}
