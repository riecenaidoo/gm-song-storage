package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.semantic.CreateResource;

/**
 * {@code Song} management is an internal {@code Service} that is automated by the system.
 * <p>
 * The public facing API is limited.
 */
public interface SongService extends CreateResource<Song> {

  /**
   * TODO define {@code UpdateResource<R>}.
   */
  Song updateSong(Song song);

}
