package com.bobo.storage.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * The atomic unit of the domain.
 *
 * <p>Represents a reference to a song available on the internet.
 *
 * <p>Encapsulates metadata about the song as well as the current status of this reference.
 */
@Entity
public class Song extends DomainEntity {

	/**
	 * The reference to the song.
	 *
	 * <p>Modeled as the Uniform Resource Locator (URL) pointing to the song on the internet.
	 *
	 * <p>Serves as the unique identifier for a {@code Song} entity.
	 */
	@Column(unique = true, nullable = false, length = 2048)
	private String url;

	/**
	 * The last time this reference was looked up.
	 *
	 * <p>For a new entity, this is expected to be {@code null} since a {@link Song} is uniquely
	 * identified by its reference.
	 */
	@SuppressWarnings("unused")
	private LocalDateTime lastLookup;

	/*
		TODO [design] Consider extracting metadata into a dedicated value object.
		This may clarify the role of a Provider — a Provider supplies SongMetadata.
		It would also support future iterations where we compare metadata across references
		to determine whether they refer to the same song.
	*/

	/**
	 * The title of the song.
	 *
	 * <p>Part of the metadata associated with this reference.
	 */
	private String title;

	/**
	 * The name of the artist.
	 *
	 * <p>Part of the metadata associated with this reference.
	 */
	private String artist;

	/**
	 * A reference to artwork for the song.
	 *
	 * <p>Represented as a URL; part of the metadata associated with this reference.
	 */
	@Column(length = 2048)
	private String thumbnailUrl;

	/**
	 * @see DomainEntity#DomainEntity()
	 */
	protected Song() {}

	/**
	 * Creates a reference to a song on the internet.
	 *
	 * @param url the Uniform Resource Locator (URL) representing the reference.
	 */
	public Song(String url) {
		this.url = validatedUrl(url);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Song song = (Song) o;
		return Objects.equals(url, song.getUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(url);
	}

	public String getUrl() {
		return url;
	}

	/**
	 * Convert this reference to a {@link URL}.
	 *
	 * @return a {@link URL} representing the reference
	 */
	public URL toUrl() {
		try {
			return toUri().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(
					"""
																							The URL of a Song is guaranteed to be a valid URL,\s
																							but it failed to be parsed into one.\s
																							Was this %s created directly into the database,
																							circumventing application logic,\s
																							or has the application logic failed to guarantee the invariant?"""
							.formatted(this.log()),
					e);
		}
	}

	/**
	 * Convert this reference to a {@link URI}, as used for constructing network requests.
	 *
	 * @return a {@link URI} representing the reference
	 */
	public URI toUri() {
		try {
			return URI.create(url);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(
					"""
																							The URL of a Song is guaranteed to be a valid URI and URL,\s
																							conforming to RFC 2396, but it failed to be converted back into a URI.\s
																							Was this %s created directly into the database,
																							circumventing application logic,\s
																							or has the application logic failed to guarantee the invariant?"""
							.formatted(this.log()),
					e);
		}
	}

	/**
	 * Validates and normalizes the given {@code url} to conform to standard URL syntax.
	 *
	 * <p>All URLs are URIs, but not all URIs are URLs. A URI identifies a resource; a URL specifies a
	 * means of locating it, typically including a scheme (like {@code https}) and authority (such as
	 * a hostname).
	 *
	 * @param rawUrl the raw input string representing a URL
	 * @return the validated and normalized URL
	 * @throws IllegalArgumentException if the input cannot be interpreted as a valid URL
	 * @see <a href="https://www.ietf.org/rfc/rfc2396.txt">RFC 2396: Uniform Resource Identifiers
	 *     (URI): Generic Syntax</a>
	 * @see <a href="https://www.ietf.org/rfc/rfc2732.txt">RFC 2732: Format for Literal IPv6 Addresses
	 *     in URLs</a>
	 */
	protected String validatedUrl(String rawUrl) throws IllegalArgumentException {
		rawUrl = rawUrl.trim();
		try {
			URI uri = new URI(rawUrl); // Syntax-level URI validation (RFC 2396)
			URL url = uri.toURL(); // Ensures absolute URL with valid scheme/host
			return url.toString();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(
					"URL validation failed. [RFC2396] Syntax issue with: %s".formatted(rawUrl), e);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(
					("""
							URL validation failed. Syntactically correct URI,
							but is not a valid URL (likely due to missing scheme or authority) : %s""")
							.formatted(rawUrl),
					e);
		}
	}

	/**
	 * Updates the reference (URL) of this song.
	 *
	 * <p>Changing the reference implies that the new URL has not been looked up before, since
	 * references uniquely identify a {@code Song}. If the reference were already known, the existing
	 * entity would be used instead.
	 *
	 * <p>As a result, this method resets {@code lastLookup} to {@code null} to indicate that the new
	 * reference requires verification.
	 *
	 * @param url the new reference URL to assign to this song
	 */
	protected void setUrl(String url) {
		this.url = validatedUrl(url);
		this.lastLookup = null;
	}

	/**
	 * Verifies the existence of this reference by performing a {@code HEAD} request on its {@code
	 * url}.
	 *
	 * <p>Follows {@code 3xx Redirection} responses and updates the {@code url} to the resolved
	 * location, if applicable. The resolved location is not immediately verified because hosts may
	 * redirect multiple times. Since the time to complete the entire redirection chain is
	 * unpredictable, further resolution is deferred to subsequent lookup cycles.
	 *
	 * <p>This operation is considered a lookup and will update {@code lastLookup}. It is intended to
	 * be invoked as part of regular lookup cycles.
	 *
	 * <p>It is not required to be called upon creation, based on the assumption that the client
	 * supplying this reference has already verified its existence at the time of submission.
	 *
	 * <p>TODO: Define behavior for HTTP responses such as {@code 501 Unauthorized}, {@code 404 Not
	 * Found}, etc. When implemented, consider changing the return type to convey the response status,
	 * or at least an enumeration grouping status codes into categories (e.g., 2xx, 3xx, 4xx, 5xx).
	 * This will allow callers to coordinate appropriately with other services. For example:
	 *
	 * <ul>
	 *   <li>3xx responses require checking if the new location is already mapped to another entity.
	 *   <li>4xx responses might trigger attempts to migrate referrals to another {@code Song} with
	 *       matching metadata.
	 * </ul>
	 *
	 * @param client the HTTP client to use; must not have a base URL configured.
	 * @return {@code true} if the {@code url} was mutated due to redirection during verification.
	 */
	public boolean verify(WebClient client) {
		ClientResponse response = client.head().uri(toUri()).exchangeToMono(Mono::just).block();
		assert response != null; // HEAD request should return response headers even on error.
		lookedUp();
		if (response.statusCode().is3xxRedirection()) {
			URI redirectionLocation = response.headers().asHttpHeaders().getLocation();
			if (redirectionLocation == null) return false;
			setUrl(redirectionLocation.toString());
			return true;
		}
		return false;
	}

	/**
	 * Called when the {@code Song} has been looked up.
	 *
	 * <p>Sets the {@code lastLookup} of the {@code Song} to {@link LocalDate#now()}.
	 */
	public void lookedUp() {
		this.lastLookup = LocalDateTime.now();
	}

	/**
	 * This mutator exists solely for internal use in testing scenarios.
	 *
	 * @param lastLookup nullable.
	 */
	protected void setLastLookup(LocalDateTime lastLookup) {
		this.lastLookup = lastLookup;
	}

	public Optional<String> getTitle() {
		return Optional.ofNullable(title);
	}

	/**
	 * @param title nullable.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public Optional<String> getArtist() {
		return Optional.ofNullable(artist);
	}

	/**
	 * @param artist nullable.
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Optional<String> getThumbnailUrl() {
		return Optional.ofNullable(thumbnailUrl);
	}

	/**
	 * @param thumbnailUrl nullable.
	 */
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = Objects.isNull(thumbnailUrl) ? null : validatedUrl(thumbnailUrl);
	}
}
