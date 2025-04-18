package com.bobo.storage.web.api.v1.request;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.web.semantic.CreateRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public record PlaylistsPostRequest(String name, Collection<String> urls) implements CreateRequest<Playlist> {

  public Playlist toCreate() {
    Collection<Song> songs = urls().stream().map(Song::new).toList();
    return new Playlist(name, songs);
  }

  public Collection<String> urls() {
    return Objects.isNull(urls) ? Collections.emptyList() : urls;
  }

}
