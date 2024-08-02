package com.bobo.storage.service;

import com.bobo.storage.domain.Playlist;

public interface PlaylistService {

  void save(Playlist playlist);

  Playlist findById(int id);
}
