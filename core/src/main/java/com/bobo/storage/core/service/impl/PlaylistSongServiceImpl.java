package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.resource.access.PlaylistSongRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PlaylistSongServiceImpl implements PlaylistSongService {

  private final PlaylistSongRepository repository;

  public PlaylistSongServiceImpl(PlaylistSongRepository repository) {
    this.repository = repository;
  }

  @Override
  public PlaylistSong create(PlaylistSong song) {
    if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();

    return repository.save(song);
  }

  @Override
  public void delete(PlaylistSong song) {
    repository.delete(song);
  }

}
