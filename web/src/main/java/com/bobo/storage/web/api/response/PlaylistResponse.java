package com.bobo.storage.web.api.response;

import com.bobo.storage.core.domain.Playlist;

import java.util.Collection;

public record PlaylistResponse(Integer id, String name, Collection<SongResponse> songs) {

  public PlaylistResponse(Playlist playlist) {
    this(playlist.getId(),
         playlist.getName(),
         playlist.getSongs().stream().map(SongResponse::new).toList());
  }

}
