package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import com.bobo.storage.core.resource.access.SongRepository;
import com.bobo.storage.core.service.PlaylistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {

  private final PlaylistRepository playlistRepository;

  private final SongRepository songRepository;

  public PlaylistServiceImpl(PlaylistRepository playlistRepository, SongRepository songRepository) {
    this.playlistRepository = playlistRepository;
    this.songRepository = songRepository;
  }

  @Override
  @Transactional
  public Playlist save(Playlist playlist) {
    playlist.setSongs(songRepository.saveAll(playlist.getSongs()));
    return playlistRepository.save(playlist);
  }

  @Override
  @Transactional
  public void addSongs(Playlist playlist, Collection<Song> songs) {
    playlist.getSongs().addAll(songs);
    save(playlist);
  }

  /**
   * Now I could just use <code>= new HashSet()</code>, but where would be the fun in that?
   * <p>
   * I would prefer not to recreate entities that are already managed in persistence context,
   * but I imagine I don't need to worry about this with the way they have implemented these things.
   * <p>
   * TODO: Consider how we expect to handle orphan removal.
   */
  @Override
  @Transactional
  public void updateSongs(Playlist playlist, Collection<Song> songs) {
    Set<Song> exclusion = playlist.getSongs().stream()
                                  .filter(Predicate.not(songs::contains))
                                  .collect(Collectors.toSet());

    playlist.getSongs().removeAll(exclusion);
    addSongs(playlist, songs);
  }

  @Override
  @Transactional
  public void updateName(Playlist playlist, String name) {
    playlist.setName(name);
    save(playlist);
  }

}
