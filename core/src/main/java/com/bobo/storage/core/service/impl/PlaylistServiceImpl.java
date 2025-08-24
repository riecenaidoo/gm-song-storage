package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.PlaylistRepository;
import com.bobo.storage.core.service.PlaylistService;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

	private final PlaylistRepository playlists;

	public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
		this.playlists = playlistRepository;
	}

	@Override
	@Transactional
	public Playlist create(Playlist playlist) {
		if (Objects.nonNull(playlist.getId())) throw new IllegalArgumentException();

		return playlists.save(playlist);
	}

	@Override
	public Playlist update(Playlist playlist) {
		return playlists.save(playlist);
	}

	/** TODO: Consider marking inactive rather than removing. */
	@Override
	@Transactional
	public void delete(Playlist playlist) {
		playlists.delete(playlist);
	}

	@Override
	public Optional<Playlist> findById(int id) {
		return playlists.findById(id);
	}

	@Override
	public Collection<Playlist> findAll() {
		return playlists.findAll();
	}

	@Override
	public Collection<Playlist> findAllByNameContainingIgnoringCase(String search) {
		return playlists.findAllByNameContainingIgnoringCase(search);
	}
}
