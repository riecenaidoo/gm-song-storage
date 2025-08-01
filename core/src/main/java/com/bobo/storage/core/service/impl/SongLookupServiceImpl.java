package com.bobo.storage.core.service.impl;

import com.bobo.semantic.TechnicalID;
import com.bobo.storage.core.domain.Provider;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import com.bobo.storage.core.service.SongLookupService;
import com.bobo.storage.core.service.SongService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SongLookupServiceImpl implements SongLookupService {

	private static final Logger log = LoggerFactory.getLogger(SongLookupServiceImpl.class);

	private final WebClient webClient;

	private final SongQueryRepository songs;

	private final SongService songService;

	private final PlaylistSongService playlistSongService;

	public SongLookupServiceImpl(
			WebClient webClient,
			SongQueryRepository songs,
			SongService songService,
			PlaylistSongService playlistSongService) {
		this.webClient = webClient;
		this.songs = songs;
		this.songService = songService;
		this.playlistSongService = playlistSongService;
	}

	/**
	 * This is the atomic operation. If it fails the batch should not fail, but this individual should
	 * be retried.
	 *
	 * <p>In the event of a redirection, we defer lookup for the next pass. It is very possible to be
	 * redirected multiple times. We should only poll Providers on a stable URL.
	 */
	@Transactional
	public void lookup(Song song) {
		HttpStatusCode statusCode = song.poll(webClient);
		if (statusCode.is2xxSuccessful()) {
			// TODO [design] Returns Optional<Provider> for later co-ordination?
			Provider.lookup(song, webClient);
			songService.updateSong(song);
			return;
		}

		if (statusCode.is3xxRedirection()) {
			Optional<Song> existingSong = songs.findByUrl(song.getUrl());
			if (existingSong.isPresent() && !TechnicalID.same(existingSong.get(), song)) {
				log.info(
						"Lookup: {} redirects to {}. Its references will be migrated to the existing Song, and it will be removed.",
						song.log(),
						existingSong.get().log());
				playlistSongService.migrate(song, existingSong.get());
				songService.delete(song);
			} else {
				log.debug("Lookup: {} redirects. URL updated. Lookup deferred.", song.log());
				songService.updateSong(song);
			}
		}
	}
}
