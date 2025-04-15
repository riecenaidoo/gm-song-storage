package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.domain.TechnicalID;

/**
 * {@code Song} management is internal {@code Service} that is automated by the system.
 * <p>
 * The public facing API is limited.
 */
public interface SongService {

  /**
   * @param song to create; must not have an assigned {@code id}.
   * @return the created {@link Song}. Equivalent to {@code song}. Not guaranteed to be the same {@code Object}.
   * @throws IllegalArgumentException if {@code song} has a {@link TechnicalID} assigned.
   */
  Song create(Song song);

}
