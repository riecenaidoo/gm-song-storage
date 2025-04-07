package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistsPostRequest;
import com.bobo.storage.web.api.request.PlaylistsPutNameRequest;
import com.bobo.storage.web.api.response.PlaylistResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@Controller(resource = Playlist.class, respondsWith = PlaylistResponse.class)
@RequestMapping("api/v1/playlists")
public class PlaylistsController {

  private final PlaylistService service;

  private final PlaylistQueryRepository playlists;

  public PlaylistsController(PlaylistService service, PlaylistQueryRepository playlists) {
    this.service = service;
    this.playlists = playlists;
  }

  /**
   * @param request to create a {@code Playlist}.
   * @return a {@code ResponseEntity} containing the {@code Playlist} that was created.
   * @see HttpStatus#CREATED
   */
  @PostMapping
  public ResponseEntity<PlaylistResponse> create(@RequestBody PlaylistsPostRequest request) {
    Playlist playlist = request.toCreate();
    playlist = service.create(playlist);
    PlaylistResponse response = new PlaylistResponse(playlist);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(String.format("/%d", playlist.getId())).build()
                                         .toUri();
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

  /**
   * @param id      of a {@code Playlist} that must exist, or {@code 400 Bad Request} ({@code 404 Not Found} would imply
   *                the target resource, {@code name} does not exist which is inaccurate).
   * @param request to update a {@code Playlist#name}.
   * @return a {@code ResponseEntity} with {@code 204 No Content} if the {@code name} was updated.
   */
  @PutMapping("/{id}/name")
  public ResponseEntity<Void> renamePlaylist(@PathVariable int id, @RequestBody PlaylistsPutNameRequest request) {
    Optional<Playlist> playlist = playlists.findById(id);
    ResponseEntity<Void> response;
    if (playlist.isPresent()) {
      service.updateName(playlist.get(), request.name());
      response = ResponseEntity.noContent().build();
    } else {
      response = ResponseEntity.badRequest().build();
    }
    return response;
  }

  /**
   * @param id of a {@code Playlist}
   * @return a {@code ResponseEntity} with {@code 204 No Content},
   * or {@code 404 Not Found} if the {@code Playlist} does not exist.
   * @see HttpStatus#NO_CONTENT
   * @see HttpStatus#NOT_FOUND
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePlaylist(@PathVariable int id) {
    Optional<Playlist> playlist = playlists.findById(id);
    ResponseEntity<Void> response;
    if (playlist.isPresent()) {
      service.delete(playlist.get());
      response = ResponseEntity.noContent().build();
    } else {
      // TODO [api] Add problem detail of 'Playlist (id: %d) does not exist.' (?)
      //  but if I do that I would have to change the method return type (ResponseEntity<Void> -> ResponseEntity<?>)
      response = ResponseEntity.notFound().build();
    }

    return response;
  }

}
