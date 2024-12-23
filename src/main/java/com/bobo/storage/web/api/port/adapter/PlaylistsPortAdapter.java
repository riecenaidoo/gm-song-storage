package com.bobo.storage.web.api.port.adapter;

import com.bobo.storage.web.api.port.PlaylistsPort;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaylistsPortAdapter implements PlaylistsPort {

  private final PlaylistService service;

  @Autowired
  public PlaylistsPortAdapter(PlaylistService service) {
    this.service = service;
  }

  @Override
  public Playlist findById(int id) {
    return service.findById(id);
  }

  @Override
  public Playlist create(String name, Collection<String> songUrls) {
    return service.save(new Playlist(name, songsOf(songUrls)));
  }

  @Override
  public void updateSongs(int id, Collection<String> songUrls) {
    service.addSongs(findById(id), songsOf(songUrls));
  }

  @Override
  public void replaceName(int id, String name) {
    service.updateName(findById(id), name);
  }

  @Override
  public void replaceSongs(int id, Collection<String> songUrls) {
    service.updateSongs(findById(id), songsOf(songUrls));
  }

  // ------ Helpers for Fluency ------

  private Set<Song> songsOf(Collection<String> urls) {
    return urls.stream()
               .map(Song::new)
               .collect(Collectors.toSet());
  }

}
