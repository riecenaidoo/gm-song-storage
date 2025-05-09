package com.bobo.storage.oembed;

import com.bobo.storage.core.domain.Song;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
      return URI.create(endpoint + "?url=" + url).toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException(
              "The instantiation of the oEmbed query, given two valid URLs, was not expected to fail.", e);
    }
  }

}
