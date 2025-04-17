package com.bobo.storage.web.api.v1.request;

import com.bobo.storage.core.domain.Song;

import java.util.Collection;

/**
 * @param op   the {@link PatchOperation} perform; never null.
 * @param urls raw URLs of the {@code Songs} in this {@code Request}; never null.
 * @see #songs()
 */
public record PlaylistSongsPatchRequest(PatchOperation op, Collection<String> urls) {

  /**
   * @return {@code Songs} from this {@code Request}.
   */
  public Collection<Song> songs() {
    return urls().stream().map(Song::new).toList();
  }

}
