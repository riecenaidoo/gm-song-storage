package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistSongsPatchRequest;
import com.bobo.storage.web.api.response.SongResponse;
import com.bobo.storage.web.exception.AssertedResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller(resource = Song.class, respondsWith = SongResponse.class)
@RequestMapping("api/v1/playlists/{id}/songs")
public class PlaylistSongsController {

  private final PlaylistService service;

  private final PlaylistQueryRepository playlists;

  public PlaylistSongsController(PlaylistService service, PlaylistQueryRepository playlists) {
    this.service = service;
    this.playlists = playlists;
  }

  @GetMapping
  public ResponseEntity<SongResponse[]> getSongs(@PathVariable int id) throws AssertedResourceNotFoundException {
    Playlist playlist = playlists.findById(id)
                                 .orElseThrow(() -> new AssertedResourceNotFoundException(Playlist.class, id));
    SongResponse[] songs = playlist.getSongs().stream()
                                   .map(SongResponse::new).toArray(SongResponse[]::new);
    return ResponseEntity.ok(songs);
  }

  /**
   * {@code PATCH#ADD} and {@code PATCH#REMOVE} might not be what I should be using for this,
   * perhaps I should be doing {@code POST}?
   */
  @Deprecated
  @PatchMapping
  public ResponseEntity<Void> updateSongs(@PathVariable int id, @RequestBody PlaylistSongsPatchRequest request)
          throws AssertedResourceNotFoundException {
    Playlist playlist = playlists.findById(id)
                                 .orElseThrow(() -> new AssertedResourceNotFoundException(Playlist.class, id));
    Collection<Song> songs = request.songs();
    switch (request.op()) {
      case ADD -> service.addSongs(playlist, songs);
      case REMOVE -> service.removeSongs(playlist, songs);
      default -> {
        return ResponseEntity.badRequest().build();
      }

    }

    return ResponseEntity.ok().build();
  }

}
