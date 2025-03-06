package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import com.bobo.storage.web.api.request.PlaylistsSongsPatchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping
  public int create(@RequestBody PlaylistsCreateRequest request) {
    Playlist playlist = service.create(new Playlist(request.name, songsOf(request.songs)));
    return playlist.getId();
  }

  @GetMapping
  public Collection<Playlist> getPlaylists() {
    return playlists.findAll();
  }

  @GetMapping("{id}")
  public Playlist getPlaylist(@PathVariable int id) {
    return playlists.findById(id).orElseThrow(() -> new RuntimeException("404"));
  }

  @GetMapping("{id}/songs")
  public Set<Song> getSongs(@PathVariable int id) {
    return getPlaylist(id).getSongs();
  }

  /**
   * Race Condition: If another request causes creation of songs, this would respond with 201?
   * <p>
   * TODO Figure out how to write a test for that?
   */
  @PatchMapping("{id}/songs")
  public ResponseEntity<Void> updateSongs(@PathVariable int id, @RequestBody PlaylistsSongsPatchRequest request) {
    final Playlist playlist = getPlaylist(id);
    final Set<Song> songs = songsOf(request.urls);
    switch (request.op) {
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

  @PutMapping("{id}/name")
  public void replaceName(@PathVariable int id, @RequestBody Map<String, String> request) {
    service.updateName(getPlaylist(id), request.get("name"));
  }

  @DeleteMapping("{id}")
  public void deletePlaylist(@PathVariable int id){
    service.delete(getPlaylist(id));
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

}
