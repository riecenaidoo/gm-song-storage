package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;

import java.util.Collection;

public interface PlaylistService {

  /**
   * Create a new <code>Playlist</code>.
   *
   * @throws RuntimeException if the <code>Playlist</code> already exists.
   */
  Playlist create(Playlist playlist) throws RuntimeException;

  @Deprecated
  void addSongs(Playlist playlist, Collection<Song> songs);

  @Deprecated
  void removeSongs(Playlist playlist, Collection<Song> songs);

  void updateName(Playlist playlist, String name);

  void delete(Playlist playlist);

}
