package com.bobo.storage.web.api.v2.response;

import com.bobo.storage.core.domain.PlaylistSong;

public record PlaylistSongResponse(Integer id, String url) {

  public PlaylistSongResponse(PlaylistSong playlistSong) {
    this(playlistSong.getId(),
         playlistSong.getSong().getUrl());
  }

}
