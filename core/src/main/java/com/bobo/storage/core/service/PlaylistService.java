package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.semantic.CreateResource;

public interface PlaylistService extends CreateResource<Playlist> {

  Playlist update(Playlist playlist);

  void delete(Playlist playlist);

}
