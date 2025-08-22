package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.DomainEntity;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.PlaylistSongRepository;
import com.bobo.storage.core.resource.query.PlaylistSongQueryRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import com.bobo.storage.core.service.SongService;
import java.util.Collection;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistSongServiceImpl implements PlaylistSongService {

	private static final Logger log = LoggerFactory.getLogger(PlaylistSongServiceImpl.class);

	private final PlaylistSongRepository repository;

	private final PlaylistSongQueryRepository query;

	private final SongService songService;

	public PlaylistSongServiceImpl(
			PlaylistSongRepository repository,
			PlaylistSongQueryRepository query,
			SongService songService) {
		this.repository = repository;
		this.query = query;
		this.songService = songService;
	}

	@Override
	@Transactional
	public PlaylistSong create(PlaylistSong song) {
		if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();
		if (song.getSong().getId() == null) {
			songService.create(song.getSong());
		}
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
		log.info(
				"PlaylistSong#Migration: {} migrated from {} to {}.",
				DomainEntity.log(songsToTransfer),
				from.log(),
				to.log());
	}
}
