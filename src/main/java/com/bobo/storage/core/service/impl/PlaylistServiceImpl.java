package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import com.bobo.storage.core.resource.access.SongRepository;
import com.bobo.storage.core.service.PlaylistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class PlaylistServiceImpl implements PlaylistService {

  private final PlaylistRepository repository;

  private final SongRepository songRepository;

  public PlaylistServiceImpl(PlaylistRepository playlistRepository, SongRepository songRepository) {
    this.repository = playlistRepository;
    this.songRepository = songRepository;
  }

  @Override
  @Transactional
  public Playlist create(Playlist playlist) {
    if(playlist.getId() != null) throw new RuntimeException("Already an Entity");
    return save(playlist);
  }

  /**
   * TODO: Trigger an async event to fetch information about these Songs from the YouTube API.
   */
  @Override
  @Transactional
  public void addSongs(Playlist playlist, Collection<Song> songs) {
    playlist.getSongs().addAll(songs);
    save(playlist);
  }

  /**
   * TODO: Consider orphan removal.
   */
  @Override
  @Transactional
  public void removeSongs(Playlist playlist, Collection<Song> songs) {
    playlist.getSongs().removeAll(songs);
    save(playlist);
  }

  @Override
  @Transactional
  public void updateName(Playlist playlist, String name) {
    playlist.setName(name);
    save(playlist);
  }

  /**
   * TODO: Consider marking inactive rather than removing.
   */
  @Override
  @Transactional
  public void delete(Playlist playlist) {
    repository.delete(playlist);
  }

  // Implementation Details

  private Playlist save(Playlist playlist) {
    playlist.setSongs(songRepository.saveAll(playlist.getSongs()));
    return repository.save(playlist);
  }

}
