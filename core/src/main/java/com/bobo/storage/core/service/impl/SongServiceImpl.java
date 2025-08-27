package com.bobo.storage.core.service.impl;

import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.SongRepository;
import com.bobo.storage.core.service.SongService;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SongServiceImpl implements SongService {

	private final SongRepository songs;

	public SongServiceImpl(SongRepository songs) {
		this.songs = songs;
	}

	/**
	 * The {@code Service} requirement is to return a {@code Song} object for the given {@code URL},
	 * but the caller does not need to know that our business rule is that there must only be a single
	 * {@code Song} to represent a {@code URL}.
	 */
	@Override
	@Transactional
	public Song add(Song song) {
		if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();

		return songs.findByUrl(song.getUrl()).orElseGet(() -> songs.save(song));
	}

	@Override
	public Optional<Song> find(int id) {
		return songs.findById(id);
	}

	@Override
	public Optional<Song> findByUrl(String url) {
		return songs.findByUrl(url);
	}

	/**
	 * @implNote In future, we will mark songs as eligible for a lookup, after a configurable period
	 *     has past since their last lookup. Those that have never been looked up will be given
	 *     priority.
	 */
	@Override
	public Collection<Song> getLookupCandidates() {
		return songs.findAllByLastLookupIsNull();
	}

	@Override
	@Transactional
	public Song updateSong(Song song) {
		return songs.save(song);
	}

	@Override
	@Transactional
	public void delete(Song song) {
		songs.delete(song);
	}
}
