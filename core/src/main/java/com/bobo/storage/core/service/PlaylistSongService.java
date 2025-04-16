package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.PlaylistSong;

public interface PlaylistSongService extends CreateResource<PlaylistSong> {

  void delete(PlaylistSong song);

}
