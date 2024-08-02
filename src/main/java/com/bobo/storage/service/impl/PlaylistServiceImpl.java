package com.bobo.storage.service.impl;

import com.bobo.storage.domain.Playlist;
import com.bobo.storage.resource.PlaylistRepository;
import com.bobo.storage.resource.SongRepository;
import com.bobo.storage.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public void save(Playlist playlist) {
    songRepository.saveAll(playlist.songs); // better than a: playlist.songs.forEach(songRepository::save);
    playlistRepository.save(playlist);
  }

  @Override
  public Playlist findById(int id) {
    return playlistRepository.findById(id).orElseThrow(() -> new RuntimeException("404"));
  }

}
