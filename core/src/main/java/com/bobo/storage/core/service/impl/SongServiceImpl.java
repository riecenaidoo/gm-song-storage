package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.SongRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.SongService;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SongServiceImpl implements SongService {

	private final SongRepository repository;

	private final SongQueryRepository query;

	public SongServiceImpl(SongRepository repository, SongQueryRepository query) {
		this.repository = repository;
		this.query = query;
	}

	/**
	 * The idea is that the {@code Service} requirement is to return a {@code Song} object for the
	 * given {@code URL}, but the caller does not need to know that our business rule is that there
	 * must only be a single {@code Song} to represent a {@code URL}.
	 */
	@Override
	@Transactional
	public Song create(Song song) {
		if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();

		return query.findByUrl(song.getUrl()).orElseGet(() -> repository.save(song));
	}

	@Override
	@Transactional
	public Song updateSong(Song song) {
		return repository.save(song);
	}

	@Override
	@Transactional
	public void delete(Song song) {
		repository.delete(song);
	}
}
