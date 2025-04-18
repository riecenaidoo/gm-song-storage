package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.semantic.CreateResource;

public interface PlaylistSongService extends CreateResource<PlaylistSong> {

  void delete(PlaylistSong song);

}
