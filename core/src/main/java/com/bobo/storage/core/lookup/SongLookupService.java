package com.bobo.storage.core.lookup;

import com.bobo.semantic.TechnicalID;
import com.bobo.storage.core.playlist.song.PlaylistSong;
import com.bobo.storage.core.playlist.song.PlaylistSongService;
import com.bobo.storage.core.song.Song;
import com.bobo.storage.core.song.SongService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Coordinates with {@link PlaylistSong} during {@link Song} lookups.
 *
 * <p>Performs coordination with the {@link PlaylistSongService} during lookup operations. While
 * each service manages CRUD operations independently, a {@code Song} lookup may trigger a merge
 * between two {@code Song} entities.
 *
 * <p>Merging occurs when:
 *
 * <ul>
 *   <li>The host of the {@code Song} URL responds with a 3xx Redirection request whose location is
 *       a URL already associated with another {@code Song}.
 *   <li>The original URL is permanently unavailable, but a replacement {@code Song}—typically from
 *       a different provider—is available.
 * </ul>
 */
@Service
public class SongLookupService {

	private static final Logger log = LoggerFactory.getLogger(SongLookupService.class);

	private final WebClient webClient;

	private final SongService songs;

	private final PlaylistSongService playlistSongs;

	public SongLookupService(
			WebClient webClient, SongService songs, PlaylistSongService playlistSongs) {
		this.webClient = webClient;
		this.songs = songs;
		this.playlistSongs = playlistSongs;
	}

	/**
	 * Performs the lookup of a {@link Song}, which is a two-step process that may require
	 * coordination with {@link PlaylistSong}.
	 *
	 * <ol>
	 *   <li>Verify the URL of the {@code Song}.
	 *   <li>Look up metadata for the {@code Song} using its URL.
	 * </ol>
	 *
	 * @param song to lookup.
	 * @see Song#poll(WebClient)
	 * @see Provider#lookup(Song, WebClient)
	 * @implNote This is the atomic operation. If it fails the batch should not fail, but this
	 *     individual should be retried.
	 *     <p>In the event of a redirection, we defer lookup for the next pass. It is very possible to
	 *     be redirected multiple times. We should only poll Providers on a stable URL.
	 */
	@Transactional
	public void lookup(Song song) {
		HttpStatusCode statusCode = song.poll(webClient);
		if (statusCode.is2xxSuccessful()) {
			// TODO [design] Returns Optional<Provider> for later co-ordination?
			Provider.lookup(song, webClient);
			songs.updateSong(song);
			return;
		}

		if (statusCode.is3xxRedirection()) {
			Optional<Song> existingSong = songs.findByUrl(song.getUrl());
			if (existingSong.isPresent() && !TechnicalID.same(existingSong.get(), song)) {
				log.info(
						"Lookup: {} redirects to {}. Its references will be migrated to the existing Song, and it will be removed.",
						song.log(),
						existingSong.get().log());
				playlistSongs.migrate(song, existingSong.get());
				songs.delete(song);
			} else {
				log.debug("Lookup: {} redirects. URL updated. Lookup deferred.", song.log());
				songs.updateSong(song);
			}
		}
	}
}
