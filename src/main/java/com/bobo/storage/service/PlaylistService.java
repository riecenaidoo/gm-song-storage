package com.bobo.storage.service;

import com.bobo.storage.domain.Playlist;

import java.util.Collection;

public interface PlaylistService {

  Playlist findById(int id);

  void save(Playlist playlist);

  default void addSongs(int id, Collection<String> songs) {
    addSongs(findById(id), songs);
  }

  void addSongs(Playlist playlist, Collection<String> songs);

  default void setSongs(int id, Collection<String> songs) {
    setSongs(findById(id), songs);
  }

  void setSongs(Playlist playlist, Collection<String> songs);

}
