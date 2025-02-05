package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/playlists")
public class PlaylistsController {

  private final PlaylistService service;

  private final PlaylistQueryRepository query;

  public PlaylistsController(PlaylistService service, PlaylistQueryRepository query) {
    this.service = service;
    this.query = query;
  }

  @PostMapping
  public int create(@RequestBody PlaylistsCreateRequest request) {
    Playlist playlist = service.save(new Playlist(request.name, songsOf(request.songs)));
    return playlist.getId();
  }

  @GetMapping("{id}")
  public Playlist get(@PathVariable int id) {
    return query.findById(id).orElseThrow(() -> new RuntimeException("404"));
  }

  @GetMapping("{id}/songs")
  public List<String> getSongs(@PathVariable int id) {
    return get(id).getSongs().stream()
                  .map((Song::getUrl)).collect(Collectors.toList());
  }

  @PatchMapping("{id}/songs")
  public void updateSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    service.updateSongs(get(id), songsOf(songs));
  }

  @PutMapping("{id}/songs")
  public void replaceSongs(@PathVariable int id, @RequestBody Collection<String> songs) {
    service.addSongs(get(id), songsOf(songs));
  }

  @PutMapping("{id}/name")
  public void replaceName(@PathVariable int id, @RequestBody String name) {
    service.updateName(get(id), name);
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
