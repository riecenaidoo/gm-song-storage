package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/playlists")
public class PlaylistsController {

  private final PlaylistService service;

  private final PlaylistQueryRepository playlists;

  public PlaylistsController(PlaylistService service, PlaylistQueryRepository playlists) {
    this.service = service;
    this.playlists = playlists;
  }

  @PostMapping
  public int create(@RequestBody PlaylistsCreateRequest request) {
    Playlist playlist = service.save(new Playlist(request.name, songsOf(request.songs)));
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
  public List<String> getSongs(@PathVariable int id) {
    return getPlaylist(id).getSongs().stream()
                          .map((Song::getUrl)).collect(Collectors.toList());
  }

  @PatchMapping("{id}/songs")
  public void updateSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    service.updateSongs(getPlaylist(id), songsOf(songs));
  }

  @PutMapping("{id}/songs")
  public void replaceSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    service.addSongs(getPlaylist(id), songsOf(songs));
  }

  @PutMapping("{id}/name")
  public void replaceName(@PathVariable int id, @RequestBody Map<String, String> request) {
    service.updateName(getPlaylist(id), request.get("name"));
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
    return urls.stream()
               .map(Song::new)
               .collect(Collectors.toSet());
  }

}
