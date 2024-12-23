package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;

import java.util.Collection;

public interface PlaylistService {

  Playlist findById(int id);

  Playlist save(Playlist playlist);

  void addSongs(Playlist playlist, Collection<Song> songs);

  void updateName(Playlist playlist, String name);

  void updateSongs(Playlist playlist, Collection<Song> songs);

}
