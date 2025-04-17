package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;

import java.util.Collection;

public interface PlaylistService extends CreateResource<Playlist>{

  @Deprecated
  void addSongs(Playlist playlist, Collection<Song> songs);

  @Deprecated
  void removeSongs(Playlist playlist, Collection<Song> songs);

  /**
   * If the {@link Playlist} can validate its state through mutators,
   * then there is no need for explicit methods in the service like this,
   * unless an update needs some kind of co-ordination between services.
   */
  @Deprecated
  void updateName(Playlist playlist, String name);

  Playlist update(Playlist playlist);

  void delete(Playlist playlist);

}
