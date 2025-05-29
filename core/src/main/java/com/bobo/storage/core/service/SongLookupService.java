package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Provider;
import com.bobo.storage.core.domain.Song;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Coordinates with {@link PlaylistSong} during {@link Song} lookups.
 *
 * <p>Performs coordination with the {@link PlaylistSongService} during lookup operations.
 * While each service manages CRUD operations independently, a {@code Song} lookup may trigger a
 * merge between two {@code Song} entities.</p>
 *
 * <p>Merging occurs when:</p>
 * <ul>
 *   <li>The host of the {@code Song} URL responds with a 3xx Redirection request
 *   whose location is a URL already associated with another {@code Song}.</li>
 *   <li>The original URL is permanently unavailable, but a replacement {@code Song}—typically from a different provider—is available.</li>
 * </ul>
 */
public interface SongLookupService {

  /**
   * Performs the lookup of a {@link Song},
   * which is a two-step process that may require coordination with {@link PlaylistSong}.
   *
   * <ol>
   *   <li>Verify the URL of the {@code Song}.</li>
   *   <li>Look up metadata for the {@code Song} using its URL.</li>
   * </ol>
   *
   * @param song to lookup.
   * @see Song#verifyUrl(WebClient)
   * @see Provider#lookupSong(Song, WebClient)
   */
  void lookup(Song song);

}
