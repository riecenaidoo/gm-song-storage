package com.bobo.storage.web.api.v1.response;

import com.bobo.storage.core.domain.Song;

public record SongResponse(String url) {

  public SongResponse(Song song) {
    this(song.getUrl());
  }

}
