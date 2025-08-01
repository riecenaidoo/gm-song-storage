package com.bobo.storage.scheduling;

import com.bobo.storage.core.domain.DomainEntity;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.SongLookupService;
import com.bobo.storage.core.service.SongService;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** The schedule of jobs the system should run, and when. */
@Component
public class JobSchedule {

	private static final Logger log = LoggerFactory.getLogger(JobSchedule.class);

	private final SongQueryRepository songs;

	private final SongService songService;

	private final SongLookupService lookupService;

	public JobSchedule(
			SongQueryRepository songs, SongService songService, SongLookupService lookupService) {
		this.songs = songs;
		this.songService = songService;
		this.lookupService = lookupService;
	}

	/**
	 * Performs the initial lookup for a {@code Song}.
	 *
	 * <p>Since metadata may not be available for every URL and isn't required in all use cases, this
	 * process is separated from the actual creation of the {@code Song}. This task runs as frequently
	 * as possible to provide near real-time feedback to the user without blocking their workflow.
	 *
	 * <p>Although the lookup of each individual {@code Song} is atomic, the job as a whole is not
	 * designed to run concurrently. Therefore, only one instance of this job should execute at any
	 * given time. The current implementation relies on Spring's default single-threaded scheduler to
	 * enforce this behavior.
	 *
	 * <p>Currently, the expected number of {@code Song} entities requiring lookup at any given time
	 * is small (typically 0-4), and the total table size remains well below 1000 entries. Because of
	 * this, batch processing or paging is not implemented. However, if the dataset grows
	 * significantly, it will be necessary to introduce paging or limit batch size to avoid
	 * performance or memory issues.
	 *
	 * <p>TODO: Refer to the <a
	 * href="https://www.postgresql.org/docs/current/indexes-partial.html">Partial Index</a>
	 * documentation for PostgreSQL to create an index targeting null values for last lookups, or
	 * explore other optimization strategies to improve query performance.
	 */
	@Scheduled(cron = "0 * * * * *")
	public void lookupNewSongs() {
		Collection<Song> songs = this.songs.findAllByLastLookupIsNull();
		if (songs.isEmpty()) {
			return;
		}
		if (log.isTraceEnabled()) {
			String urlQueue = songs.stream().map(Song::getUrl).collect(Collectors.joining("\n\t - "));
			log.trace("Job#LookupNewSongs: Looking up {}...\n\t - {}", DomainEntity.log(songs), urlQueue);
		} else if (log.isInfoEnabled()) {
			log.info("Job#LookupNewSongs: Looking up {}...", DomainEntity.log(songs));
		}

		for (Song song : songs) {
			try {
				lookupService.lookup(song);
			} catch (Exception ex) {
				log.error(
						"Job#LookupNewSongs: Exception encountered on {} with url {}",
						song.log(),
						song.getUrl(),
						ex);

				Optional<Song> originalSong = this.songs.findById(song.getId());
				if (originalSong.isPresent()) {
					song = originalSong.get();
					song.lookedUp();
					songService.updateSong(song);
					log.info(
							"Job#LookupNewSongs: Gracefully handled exception on {}. Removed from lookup queue.",
							song.log());
				} else {
					log.warn(
							"""
													Job#LookupNewSongs: Exception handling failed because {} is no longer
														present in the repository. Was it removed while lookup was occurring?""",
							song.log());
				}
			}
		}
	}
}
