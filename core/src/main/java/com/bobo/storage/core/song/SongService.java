package com.bobo.storage.core.song;

import com.bobo.storage.core.domain.DomainEntity;
import com.bobo.storage.core.semantic.Create;
import com.bobo.storage.core.semantic.Read;
import com.bobo.storage.core.service.SongLookupService;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SongService implements Create<Song>, Read<Song> {

	private final SongRepository songs;

	public SongService(SongRepository songs) {
		this.songs = songs;
	}

	/**
	 * Songs are uniquely identified by their {@code url}; only one {@link Song} with a given {@code
	 * url} can exist in the system at any time.
	 *
	 * @implSpec {@link Create#add(DomainEntity)}
	 */
	@Override
	@Transactional
	public Song add(Song song) {
		if (Objects.nonNull(song.getId())) throw new IllegalArgumentException();

		return songs.findByUrl(song.getUrl()).orElseGet(() -> songs.save(song));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Song> find(int id) {
		return songs.findById(id);
	}

	/**
	 * Find a {@link Song} by its {@code url}, which uniquely identifies it.
	 *
	 * @param url the unique reference {@code url} of the {@link Song}.
	 * @return the {@link Song} if found, otherwise Optional.empty().
	 * @implSpec {@link Read#find(int)}
	 */
	@Transactional(readOnly = true)
	public Optional<Song> findByUrl(String url) {
		return songs.findByUrl(url);
	}

	/**
	 * @return a {@link Collection} of all {@link Song} resources that are eligible for a lookup;
	 *     never null.
	 * @see Song#lookedUp()
	 * @see SongLookupService
	 * @apiNote In future, we will accept a {@code limit} parameter to control the return size.
	 * @implSpec {@link Read#get()}
	 * @implNote In future, we will mark songs as eligible for a lookup, after a configurable period
	 *     has past since their last lookup. Those that have never been looked up will be given
	 *     priority.
	 */
	@Transactional(readOnly = true)
	public Collection<Song> getLookupCandidates() {
		return songs.findAllByLastLookupIsNull();
	}

	/** TODO define {@code UpdateResource<R>}. */
	@Transactional
	public Song updateSong(Song song) {
		return songs.save(song);
	}

	@Transactional
	public void delete(Song song) {
		songs.delete(song);
	}
}
