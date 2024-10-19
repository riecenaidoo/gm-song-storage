package com.bobo.storage.service;

import com.bobo.storage.domain.Playlist;

import java.util.Collection;

public interface PlaylistService {

  Playlist findById(int id);

  void save(Playlist playlist);

  default void updateSongs(int id, Collection<String> songs) {
    updateSongs(findById(id), songs);
  }

  void updateSongs(Playlist playlist, Collection<String> songs);

  default void addSongs(int id, Collection<String> songs) {
    addSongs(findById(id), songs);
  }

  void addSongs(Playlist playlist, Collection<String> songs);

}
