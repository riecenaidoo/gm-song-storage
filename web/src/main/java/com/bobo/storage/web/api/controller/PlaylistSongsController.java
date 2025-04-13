package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistSongsPatchRequest;
import com.bobo.storage.web.api.response.SongResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

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
  public ResponseEntity<SongResponse[]> getSongs(@PathVariable int id) {
    Optional<Playlist> playlist = playlists.findById(id);
    if (playlist.isPresent()) {
      SongResponse[] songs = playlist.get().getSongs().stream()
                                     .map(SongResponse::new).toArray(SongResponse[]::new);
      return ResponseEntity.ok(songs);
    } else {
      /*  TODO [design]
          This situation represents an exception to the normal flow of this method,
          and in fact all controller methods serving sub resources. Though this controller could possible
          'handle' this situation by returning the appropriate ResponseEntity, this is better done using an ExceptionHandler
          and throwing the appropriate exception for this circumstance.

          I would probably want a signature in like #get that throws an 'AssertedResourceNotFound',
          that I can map to 400 Bad Request.
          TODO [housekeeping] I think I have another note like this elsewhere already.
       */
      return ResponseEntity.badRequest()
                           .build();   // Another pain point with this is that the generically typed return is a lie now.
    }
  }

  /**
   * {@code PATCH#ADD} and {@code PATCH#REMOVE} might not be what I should be using for this,
   * perhaps I should be doing {@code POST}?
   */
  @Deprecated
  @PatchMapping
  public ResponseEntity<Void> updateSongs(@PathVariable int id, @RequestBody PlaylistSongsPatchRequest request) {
    final Optional<Playlist> assertedPlaylist = playlists.findById(id);
    if (assertedPlaylist.isPresent()) {
      Playlist playlist = assertedPlaylist.get();

      final Collection<Song> songs = request.songs();
      switch (request.op()) {
        case ADD -> service.addSongs(playlist, songs);
        case REMOVE -> service.removeSongs(playlist, songs);
        default -> {
          return ResponseEntity.badRequest().build(); // TODO Some generic error for this; unsupported operation?
        }
      }

      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

}
