package com.bobo.storage.web.controller;

import com.bobo.storage.domain.Playlist;
import com.bobo.storage.domain.Song;
import com.bobo.storage.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/playlists")
public class PlaylistRestController {

  private final PlaylistService playlistService;

  @Autowired
  public PlaylistRestController(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  @PostMapping
  public int create(@RequestBody Playlist playlist) {
    playlistService.save(playlist);
    return playlist.id;
  }

  @GetMapping("{id}")
  public Playlist fetch(@PathVariable int id) {
    return playlistService.findById(id);
  }

  @GetMapping("{id}/songs")
  public List<String> fetchSongs(@PathVariable int id) {
    return fetch(id).songs.stream()
                          .map((Song::getUrl)).collect(Collectors.toList());
  }

  @PatchMapping("{id}/songs")
  public void updateSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    playlistService.addSongs(id, songs);
  }

  @PutMapping("{id}/name")
  public void replaceName(@PathVariable int id, @RequestBody String name) {
    Playlist playlist = fetch(id);
    playlist.name = name;
    playlistService.save(playlist);
  }

  @PutMapping("{id}/songs")
  public void replaceSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    playlistService.setSongs(id, songs);
  }

}
