package com.bobo.storage.api.port;

import com.bobo.storage.domain.Playlist;

import java.util.Collection;

public interface PlaylistsPort {

  Playlist findById(int id);

  Playlist create(String name, Collection<String> songs);

  void updateSongs(int id, Collection<String> songs);

  void replaceName(int id, String name);

  void replaceSongs(int id, Collection<String> songs);

}
