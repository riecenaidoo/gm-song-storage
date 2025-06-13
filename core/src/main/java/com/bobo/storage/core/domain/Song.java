package com.bobo.storage.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The atomic unit of the domain.
 * <p>
 * Represents a reference to a Song on the internet.
 * <p>
 * In future iterations this abstraction would also represent information about the Song.
 * In its current form, it could be considered a Value Object.
 */
@Entity
public class Song extends DomainEntity {

  /**
   * Uniform Resource Locator (URL) to the Song on the internet.
   * <p>
   * Currently serves as the unique identifier for a Song entity.
   * However, in future iterations, we would likely have a collection of URLs.
   * The long term goal of the system is to find and manage multiple references
   * to a song on the internet across different vendors.
   * At that stage we would likely need to introduce a technical id.
   */
  private String url;

  /**
   * When was this {@code url} last verified (looked up)?
   */
  private LocalDateTime lastLookup;

  private String title;

  private String artist;

  private String thumbnailUrl;

  /**
   * @see DomainEntity#DomainEntity()
   */
  protected Song() {
  }

  /**
   * Create a reference to a Song on the internet.
   *
   * @param url Uniform Resource Locator (URL) to the Song on the internet.
   */
  public Song(String url) {
    this.url = url;
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

  @Column(unique = true, nullable = false, length = 2048)
  public String getUrl() {
    return url;
  }

  protected void setUrl(String url) {
    this.url = url;
  }

  /**
   * Perform a {@code HEAD} request on this ({@code url}) {@code Song} to verify its existence.
   * <p>
   * Will attempt to resolve {@code 3xx Redirection} responses by updating the {@code url} to the redirection location.
   * <p>
   * TODO Consider what do do for unauthorized, or not found requests, etc.
   * TODO This needs to be run on creation, but also regularly as part of look-ups. This should update last-lookup,
   *  but that being null is used to run the first lookup hook. Need to question that.
   *
   * @param client to perform the request. Should not have any base path configured.
   * @return {@code true} if the {@code Song} was mutated during resolution of the verification.
   */
  public boolean verifyUrl(WebClient client){
    URI uri;
    try{
      uri = URI.create(url);
    }catch (IllegalArgumentException e){
      /*
       TODO We should guarantee that the URL given can be formed into a URI,
        and then provide an accessor signature that squashes the exception (because we will already have run validation
        on it.
      */
      throw new RuntimeException("Unexpected.");
    }

    ClientResponse response = client.head().uri(uri).exchangeToMono(Mono::just).block();
    assert response != null; // HEAD request should return a response headers even on error.
    if (response.statusCode().is3xxRedirection()) {
      URI redirectionLocation = response.headers().asHttpHeaders().getLocation();
      if (redirectionLocation == null) return false;
      try {
        url = redirectionLocation.toURL().toString(); // TODO Consider how we model url and then use a setter (?)
        this.lastLookup = null; // TODO this should be atomic in the setter for URL. If we change the URL it must be looked up again.
        return true;
      } catch (MalformedURLException e) {
        throw new RuntimeException("Unexpected. The redirection local was malformed.");
      }
    }

    return false;
  }

  // TODO All of these should be Optional, but I struggle to map them accordingly.

  /**
   * Declared for JPA mapping.
   * <p>
   * This is a meta-data field that is queried against to regularly validate the existence of a URL,
   * it should not be visible beyond the bounds of this module.
   */
  protected LocalDateTime getLastLookup() {
    return lastLookup;
  }

  public void setLastLookup(LocalDateTime lastLookup) {
    this.lastLookup = lastLookup;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  @Column(length = 2048)
  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

}
