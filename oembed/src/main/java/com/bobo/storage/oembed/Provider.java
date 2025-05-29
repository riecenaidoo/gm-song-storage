package com.bobo.storage.oembed;

import com.bobo.storage.core.domain.Song;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The source of an embeddable form of content that adheres to the {@code oEmbed} specification.
 * <p>
 * In this domain - the source of a {@link Song}'s {@code URL};
 * a {@code Provider} would be the {@code host} of that {@code URL}.
 * <p>
 * The name of each enumeration corresponds to the {@code hostname} of the {@code Provider}.
 * <p>
 * Not all {@code hosts} support {@code oEmbed},
 * and neither are all {@code oEmbed} {@code Providers} are codified in this enumeration.
 *
 * @see <a href="https://oembed.com/#section7.1">oEmbed > 7.1 Providers</a>
 */
public enum Provider {

  YOUTUBE("https://www.youtube.com/oembed"),
  DEEZER("https://api.deezer.com/oembed"),
  SPOTIFY("https://open.spotify.com/oembed");

  /**
   * An {@code endpoint} exposed by the {@code host} that adheres to the {@code oEmbed} specification.
   */
  private final URL endpoint;

  Provider(String endpoint) {
    try {
      this.endpoint = URI.create(endpoint).toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException("Check the endpoint argument, there is probably a mis-input.", e);
    }
  }

  /**
   * Marked {@code protected} to expose for testing.
   */
  @SuppressWarnings("ProtectedMemberInFinalClass")
  protected URL getEndpoint() {
    return endpoint;
  }

  public URL getQuery(URL url) {
    try {
      return URI.create(endpoint + "?url=" + url + "&format=json").toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException(
              "The instantiation of the oEmbed query, given two valid URLs, was not expected to fail.", e);
    }
  }

  /**
   * Looks up metadata for a Song, if data is found the Song will be mutated with the information found.
   * It is the caller's responsibility to handle {@link Song#setLastLookup(LocalDateTime)}.
   *
   * @return true if information about the Song was found.
   */
  public static boolean lookupSong(Song song, WebClient webClient) {
    URL url;
    try {
      url = URI.create(song.getUrl()).toURL();
    } catch (MalformedURLException e) {
      // TODO [design] the Song URL may be stored as a String, but it should probably be typed to a URL.
      throw new RuntimeException("The URL of the Song was invalid.");
    }

    for (Provider provider : values()) {
      Optional<OEmbedResponse> metadata = provider.queryProvider(url, webClient);
      if (metadata.isPresent()) {
        metadata.get().title().ifPresent(song::setTitle);
        metadata.get().authorName().ifPresent(song::setArtist);
        metadata.get().thumbnailUrl().ifPresent(song::setThumbnailUrl);
        return true;
      }
    }
    return false;
  }

  public Optional<OEmbedResponse> queryProvider(URL url, WebClient webClient) {
    URL query = getQuery(url);
    URI queryURI;
    try {
      queryURI = query.toURI();
    } catch (URISyntaxException e) {
      // TODO [design] Should probably question whether #getQuery should return a URL or URI then.
      throw new RuntimeException();
    }

    return webClient.get().uri(queryURI)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono(Provider::toMono)
                    .blockOptional();
  }

  /**
   * Extracted for method referencing.
   *
   * @param response from an oEmbed API.
   * @return a {@link Mono} of an {@link OEmbedResponse},
   * if the API responded with {@code 200 OK} otherwise {@link Mono#empty()}.
   */
  private static Mono<OEmbedResponse> toMono(ClientResponse response) {
    return (response.statusCode().equals(HttpStatus.OK)) ? response.bodyToMono(OEmbedResponse.class) : Mono.empty();
  }

}
