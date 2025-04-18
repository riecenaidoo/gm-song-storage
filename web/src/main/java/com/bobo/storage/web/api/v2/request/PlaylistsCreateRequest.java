package com.bobo.storage.web.api.v2.request;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.web.api.v1.request.CreateRequest;

public record PlaylistsCreateRequest(String title) implements CreateRequest<Playlist> {

  @Override
  public Playlist toCreate() {
    return new Playlist(title);
  }

}
