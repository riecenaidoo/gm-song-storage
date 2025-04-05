package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import com.bobo.storage.web.api.request.PlaylistsPutNameRequest;
import com.bobo.storage.web.api.request.PlaylistsSongsPatchRequest;
import com.bobo.storage.web.api.response.PlaylistResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/playlists")
public class PlaylistsController {

  private final PlaylistService service;

  private final PlaylistQueryRepository playlists;

  private final SongQueryRepository songs;

  public PlaylistsController(PlaylistService service, PlaylistQueryRepository playlists, SongQueryRepository songs) {
    this.service = service;
    this.playlists = playlists;
    this.songs = songs;
  }

  /**
   * @param request to create a {@code Playlist}.
   * @return a {@code ResponseEntity} containing the {@code Playlist} that was created.
   * @see HttpStatus#CREATED
   */
  @PostMapping
  public ResponseEntity<PlaylistResponse> create(@RequestBody PlaylistsCreateRequest request) {
    Playlist playlist = service.create(new Playlist(request.name(), songsOf(request.songs())));
    PlaylistResponse response = new PlaylistResponse(playlist);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                                         .path(String.format("/%d", playlist.getId()))
                                         .build().toUri();
    return ResponseEntity.created(uri).body(response);
  }

  /**
   * @return a {@code ResponseEntity} containing an {@code Array} of zero or more {@code Playlist}(s).
   */
  @GetMapping
  public ResponseEntity<PlaylistResponse[]> getPlaylists() {
    Collection<Playlist> playlists = this.playlists.findAll();
    PlaylistResponse[] response = playlists.stream().map(PlaylistResponse::new).toArray(PlaylistResponse[]::new);
    return ResponseEntity.ok(response);
  }

  /**
   * @param id of a {@code Playlist}
   * @return a {@code ResponseEntity} containing the {@code Playlist}, if it exists.
   * @see HttpStatus#NOT_FOUND
   * @see ResponseEntity#of(Optional)
   */
  @GetMapping("/{id}")
  public ResponseEntity<PlaylistResponse> getPlaylist(@PathVariable int id) {
    Optional<Playlist> playlist = playlists.findById(id);
    Optional<PlaylistResponse> response = playlist.map(PlaylistResponse::new);
    return ResponseEntity.of(response);
  }

  @GetMapping("/{id}/songs")
  public Set<Song> getSongs(@PathVariable int id) {
    return findById(id).getSongs();
  }

  /**
   * Race Condition: If another request causes creation of songs, this would respond with 201?
   * <p>
   * TODO Figure out how to write a test for that?
   */
  @PatchMapping("/{id}/songs")
  public ResponseEntity<Void> updateSongs(@PathVariable int id, @RequestBody PlaylistsSongsPatchRequest request) {
    final Playlist playlist = findById(id);
    final Set<Song> songs = songsOf(request.urls());
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

  @PutMapping("/{id}/name")
  public void renamePlaylist(@PathVariable int id, @RequestBody PlaylistsPutNameRequest request) {
    service.updateName(findById(id), request.name());
  }

  @DeleteMapping("/{id}")
  public void deletePlaylist(@PathVariable int id) {
    service.delete(findById(id));
  }

  // ------ Helpers for Fluency ------

  /**
   * The <code>songs</code> parameters in this controller represent the <code>Song.url</code> field
   * of the <code>Song</code> entity.
   *
   * @param urls to map to <code>Song</code>(s)
   * @return distinct collection of <code>Song</code>(s) objects. They are not yet managed entities.
   */
  private Set<Song> songsOf(Collection<String> urls) {
    if (Objects.isNull(urls)) return Collections.emptySet();
    return urls.stream()
               .map(Song::new)
               .collect(Collectors.toSet());
  }

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
