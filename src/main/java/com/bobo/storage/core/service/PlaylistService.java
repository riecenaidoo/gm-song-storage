package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;

import java.util.Collection;

public interface PlaylistService {

  Playlist save(Playlist playlist);

  void addSongs(Playlist playlist, Collection<Song> songs);

  void removeSongs(Playlist playlist, Collection<Song> songs);

  void updateName(Playlist playlist, String name);

}
