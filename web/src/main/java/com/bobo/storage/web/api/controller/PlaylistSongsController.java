package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistSongsPatchRequest;
import com.bobo.storage.web.api.response.SongResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@Controller(resource = Song.class, respondsWith = SongResponse.class)
@RequestMapping("api/v1/playlists/{id}/songs")
public class PlaylistSongsController {

  private final PlaylistService service;

  private final PlaylistQueryRepository playlists;

  private final SongQueryRepository songs;

  public PlaylistSongsController(PlaylistService service, PlaylistQueryRepository playlists, SongQueryRepository songs) {
    this.service = service;
    this.playlists = playlists;
    this.songs = songs;
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
      return ResponseEntity.badRequest().build();   // Another pain point with this is that the generically typed return is a lie now.
    }
  }

  /**
   * Race Condition: If another request causes creation of songs, this would respond with 201?
   * <p>
   * TODO Figure out how to write a test for that?
   */
  @PatchMapping
  public ResponseEntity<Void> updateSongs(@PathVariable int id, @RequestBody PlaylistSongsPatchRequest request) {
    final Playlist playlist = findById(id);
    final Collection<Song> songs = request.songs();
    switch (request.op()) {
      case ADD -> {
        long existingSongs = this.songs.count();
        service.addSongs(playlist, songs);
        return ResponseEntity.status((this.songs.count() == existingSongs ? 200 : 201)).build();
      }
      case REMOVE -> service.removeSongs(playlist, songs);
      default -> throw new RuntimeException("501");
    }

    return ResponseEntity.ok().build();
  }

  // ------ Helpers for Fluency ------

  /**
   * Added as patch in the interim to prevent breaking other controllers.
   * Will be revised or removed entirely.
   *
   * @throws RuntimeException if a {@link Playlist} with the given {@code id} does not exist.
   * @see HttpStatus#NOT_FOUND
   */
  @Deprecated
  private Playlist findById(int id) throws RuntimeException {
    Optional<Playlist> playlist = playlists.findById(id);
    if (playlist.isPresent()) return playlist.get();
    throw new RuntimeException(String.format("404: Playlist (id: %d) does not exist.", id));
  }

}
