package com.bobo.storage.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

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

  @Column(unique = true, nullable = false)
  public String getUrl() {
    return url;
  }

  protected void setUrl(String url) {
    this.url = url;
  }

}
