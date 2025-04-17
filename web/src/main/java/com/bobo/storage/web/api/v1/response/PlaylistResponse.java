package com.bobo.storage.web.api.v1.response;

import com.bobo.storage.core.domain.Playlist;

public record PlaylistResponse(Integer id, String name, SongResponse[] songs) {

  public PlaylistResponse(Playlist playlist) {
    this(playlist.getId(),
         playlist.getName(),
         playlist.getSongs().stream().map(SongResponse::new).toArray(SongResponse[]::new));
  }

}
