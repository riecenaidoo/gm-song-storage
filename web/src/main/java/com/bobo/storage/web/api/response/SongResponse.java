package com.bobo.storage.web.api.response;

import com.bobo.storage.core.domain.Song;

public record SongResponse(String url) {

  public SongResponse(Song song) {
    this(song.getUrl());
  }

}
