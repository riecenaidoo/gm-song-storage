package com.bobo.storage.web.api.v2.response;

import com.bobo.storage.core.domain.PlaylistSong;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * Where {@code Optional} fields are only included in the response if they have a value.
 * <p>
 * TODO Consider defining this globally.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record PlaylistSongResponse(Integer id, String url,
                                   Optional<String> title,
                                   Optional<String> artist,
                                   @JsonProperty("thumbnail_url") Optional<String> thumbnailUrl) {

  public PlaylistSongResponse(PlaylistSong playlistSong) {
    this(playlistSong.getId(),
         playlistSong.getSong().getUrl(),
         Optional.ofNullable(playlistSong.getSong().getTitle()),
         Optional.ofNullable(playlistSong.getSong().getArtist()),
         Optional.ofNullable(playlistSong.getSong().getThumbnailUrl())
    );
  }

}
