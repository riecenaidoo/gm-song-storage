package com.bobo.storage.service.impl;

import com.bobo.storage.domain.Playlist;
import com.bobo.storage.domain.Song;
import com.bobo.storage.resource.PlaylistRepository;
import com.bobo.storage.resource.SongRepository;
import com.bobo.storage.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {

  private final PlaylistRepository playlistRepository;

  private final SongRepository songRepository;

  @Autowired
  public PlaylistServiceImpl(PlaylistRepository playlistRepository, SongRepository songRepository) {
    this.playlistRepository = playlistRepository;
    this.songRepository = songRepository;
  }

  @Override
  public Playlist findById(int id) {
    return playlistRepository.findById(id).orElseThrow(() -> new RuntimeException("404"));
  }

  @Override
  public void save(Playlist playlist) {
    songRepository.saveAll(playlist.songs);
    playlistRepository.save(playlist);
  }

  @Override
  public void addSongs(Playlist playlist, Collection<String> songs) {
    songs.stream()
         .map(Song::new)
         .forEach(playlist.songs::add);
    save(playlist);
  }

  /**
   * Now I could just use <code>= new HashSet()</code>, but where would be the fun in that?
   * <p>
   * I would prefer not to recreate entities that are already managed in persistence context,
   * but I imagine I don't need to worry about this with the way they have implemented these things.
   */
  @Override
  public void setSongs(Playlist playlist, Collection<String> songs) {
    Set<Song> exclusion = playlist.songs.stream()
                                        .filter(song -> !songs.contains(song.url))
                                        .collect(Collectors.toSet());

    playlist.songs.removeAll(exclusion);
    addSongs(playlist, songs);
    //    songRepository.deleteAll(exclusion); // TODO: This would be need to be done as orphan removal.
  }
}
