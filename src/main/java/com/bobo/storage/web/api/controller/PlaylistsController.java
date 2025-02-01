package com.bobo.storage.web.api.controller;

import com.bobo.storage.web.api.port.PlaylistsPort;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/playlists")
public class PlaylistsController {

  private final PlaylistsPort playlists;

  @Autowired
  public PlaylistsController(PlaylistsPort playlists) {
    this.playlists = playlists;
  }

  @PostMapping
  public int create(@RequestBody PlaylistsCreateRequest request) {
    return playlists.create(request.name, request.songs).id;
  }

  @GetMapping("{id}")
  public Playlist get(@PathVariable int id) {
    return playlists.findById(id);
  }

  @GetMapping("{id}/songs")
  public List<String> getSongs(@PathVariable int id) {
    return get(id).songs.stream()
                        .map((Song::getUrl)).collect(Collectors.toList());
  }

  @PatchMapping("{id}/songs")
  public void updateSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    playlists.updateSongs(id, songs);
  }

  @PutMapping("{id}/songs")
  public void replaceSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    playlists.replaceSongs(id, songs);
  }

  @PutMapping("{id}/name")
  public void replaceName(@PathVariable int id, @RequestBody String name) {
    playlists.replaceName(id, name);
  }

}
