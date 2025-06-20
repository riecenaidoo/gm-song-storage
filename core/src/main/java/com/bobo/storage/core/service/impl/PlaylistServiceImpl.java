package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import com.bobo.storage.core.service.PlaylistService;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

	private final PlaylistRepository repository;

	public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
		this.repository = playlistRepository;
	}

	@Override
	@Transactional
	public Playlist create(Playlist playlist) {
		if (Objects.nonNull(playlist.getId())) throw new IllegalArgumentException();

		return repository.save(playlist);
	}

	@Override
	public Playlist update(Playlist playlist) {
		return repository.save(playlist);
	}

	/** TODO: Consider marking inactive rather than removing. */
	@Override
	@Transactional
	public void delete(Playlist playlist) {
		repository.delete(playlist);
	}
}
