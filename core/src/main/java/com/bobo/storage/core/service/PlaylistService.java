package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.semantic.CreateResource;
import java.util.Collection;
import java.util.Optional;

public interface PlaylistService extends CreateResource<Playlist> {

	Playlist update(Playlist playlist);

	void delete(Playlist playlist);

	Optional<Playlist> findById(int id);

	Collection<Playlist> findAll();

	Collection<Playlist> findAllByNameContainingIgnoringCase(String search);
}
