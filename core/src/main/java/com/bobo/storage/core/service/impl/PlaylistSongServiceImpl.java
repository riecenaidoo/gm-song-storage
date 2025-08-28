package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.DomainEntity;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.PlaylistSongRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import com.bobo.storage.core.service.SongService;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistSongServiceImpl implements PlaylistSongService {

	private static final Logger log = LoggerFactory.getLogger(PlaylistSongServiceImpl.class);

	private final PlaylistSongRepository playlistSongs;

	private final SongService songs;

	public PlaylistSongServiceImpl(PlaylistSongRepository playlistSongs, SongService songs) {
		this.playlistSongs = playlistSongs;
		this.songs = songs;
	}

	@Override
	@Transactional
	public PlaylistSong add(PlaylistSong song) {
		if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();
		if (song.getSong().getId() == null) {
			songs.add(song.getSong());
		}
		return playlistSongs.save(song);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PlaylistSong> find(int id) {
		return playlistSongs.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<PlaylistSong> getFromPlaylist(Playlist playlist) {
		return playlistSongs.findAllByPlaylist(playlist);
	}

	@Override
	@Transactional
	public void migrate(Song from, Song to) {
		Collection<PlaylistSong> songsToTransfer = playlistSongs.findAllBySong(from);
		songsToTransfer.forEach(song -> song.migrate(to));
		playlistSongs.saveAll(songsToTransfer);
		log.info(
				"PlaylistSong#Migration: {} migrated from {} to {}.",
				DomainEntity.log(songsToTransfer),
				from.log(),
				to.log());
	}

	@Override
	@Transactional
	public void delete(PlaylistSong song) {
		playlistSongs.delete(song);
	}
}
