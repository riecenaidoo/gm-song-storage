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

  /**
   * TODO: Trigger an async event to fetch information about these Songs from the YouTube API.
   */
  @Override
  @Transactional
  public void addSongs(Playlist playlist, Collection<Song> songs) {
    playlist.getSongs().addAll(songs);
    save(playlist);
  }

  @Override
  @Transactional
  public void updateName(Playlist playlist, String name) {
    playlist.setName(name);
    save(playlist);
  }

}
