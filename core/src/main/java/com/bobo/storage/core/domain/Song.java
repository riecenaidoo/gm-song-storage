package com.bobo.storage.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Song {

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
   * JSR 338: Java Persistence API (JPA) Specification (v.2.2):
   * <p>
   * 2.1 The Entity Class:
   * "The entity class must have a no-arg constructor.
   * The entity class may have other constructors as well.
   * The no-arg constructor must be public or protected."
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

  /**
   * JSR 338: Java Persistence API (JPA) Specification (v.2.2):
   * <p>
   * 2.3.1 Default Access Type:
   * <p>
   * "The default access type of an entity hierarchy is determined by the placement of mapping annotations
   * on the attributes of the entity classes."
   * <p>
   * We could manually configure this using <code>@Access(AccessType.PROPERTY)</code> on the class definition.
   */
  @Id
  public String getUrl() {
    return url;
  }

  /**
   * This property (mutator) will be used dynamically by the JPA provider to populate this object
   * after construction via the no-args constructor.
   */
  @SuppressWarnings("unused")
  protected void setUrl(String url) {
    this.url = url;
  }

}
