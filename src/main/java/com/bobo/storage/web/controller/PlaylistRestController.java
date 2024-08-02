package com.bobo.storage.web.controller;

import com.bobo.storage.domain.Playlist;
import com.bobo.storage.domain.Song;
import com.bobo.storage.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    return playlistService.findById(id)
            .songs.stream()
            .map((Song::getUrl))
            .collect(Collectors.toList());
  }

  @PutMapping("{id}/songs")
  public void updateSongs(@PathVariable int id, @RequestBody List<String> songs) {
    Playlist playlist = playlistService.findById(id);
    songs.stream()
            .map(Song::new)
            .forEach(playlist.songs::add);
    playlistService.save(playlist);
  }

  @PutMapping("{id}/name")
  public void updateName(@PathVariable int id, @RequestBody String name) {
    Playlist playlist = playlistService.findById(id);
    playlist.name = name;
    playlistService.save(playlist);
  }

}
