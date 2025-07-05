package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.PlaylistSongRepository;
import com.bobo.storage.core.resource.query.PlaylistSongQueryRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import java.util.Collection;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistSongServiceImpl implements PlaylistSongService {

	private final PlaylistSongRepository repository;

	private final PlaylistSongQueryRepository query;

	public PlaylistSongServiceImpl(
			PlaylistSongRepository repository, PlaylistSongQueryRepository query) {
		this.repository = repository;
		this.query = query;
	}

	@Override
	@Transactional
	public PlaylistSong create(PlaylistSong song) {
		if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();

		return repository.save(song);
	}

	@Override
	@Transactional
	public void delete(PlaylistSong song) {
		repository.delete(song);
	}

	@Override
	@Transactional
	public void migrate(Song from, Song to) {
		Collection<PlaylistSong> songsToTransfer = query.findAllBySong(from);
		songsToTransfer.forEach(song -> song.migrate(to));
		repository.saveAll(songsToTransfer);
	}
}
