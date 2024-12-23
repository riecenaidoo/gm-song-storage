package com.bobo.storage.service;

import com.bobo.storage.domain.Playlist;
import com.bobo.storage.domain.Song;

import java.util.Collection;

public interface PlaylistService {

  Playlist findById(int id);

  Playlist save(Playlist playlist);

  void addSongs(Playlist playlist, Collection<Song> songs);

  void updateName(Playlist playlist, String name);

  void updateSongs(Playlist playlist, Collection<Song> songs);

}
