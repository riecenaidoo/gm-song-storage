package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.PlaylistRepository;
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
	public Playlist add(Playlist playlist) {
		if (Objects.nonNull(playlist.getId())) throw new IllegalArgumentException();

		return playlists.save(playlist);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Playlist> find(int id) {
		return playlists.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Playlist> get() {
		return playlists.findAll();
	}

	/**
	 * @implNote We should explore support for more advanced search functionality in the future,
	 *     including semantic search via external libraries or embedded LLMs.
	 *     <p>We should also consider introducing relevance scoring to rank results based on match
	 *     quality.
	 */
	@Override
	@Transactional(readOnly = true)
	public Collection<Playlist> searchByName(String name) {
		return playlists.findAllByNameContainingIgnoringCase(name);
	}

	@Override
	@Transactional
	public Playlist update(Playlist playlist) {
		return playlists.save(playlist);
	}

	/** TODO: Consider marking inactive rather than removing. */
	@Override
	@Transactional
	public void delete(Playlist playlist) {
		playlists.delete(playlist);
	}
}
