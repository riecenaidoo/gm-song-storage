package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.SongRepository;
import com.bobo.storage.core.service.SongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class SongServiceImpl implements SongService {

  private final SongRepository repository;

  public SongServiceImpl(SongRepository repository) {
    this.repository = repository;
  }

  /**
   * The idea is that the {@code Service} requirement is to return a {@code Song} object for the given {@code URL},
   * but the caller does not need to know that our business rule is that there must only be a single {@code Song} to
   * represent a {@code URL}.
   */
  @Override
  @Transactional
  public Song create(Song song) {
    if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();

    return repository.findByUrl(song.getUrl()).orElseGet(() -> repository.save(song));
  }

}
