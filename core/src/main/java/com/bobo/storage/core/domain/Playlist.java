package com.bobo.storage.core.domain;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Playlist {

  private Integer id;

  private Set<Song> songs;

  private String name;

  /**
   * JSR 338: Java Persistence API (JPA) Specification (v.2.2):
   * <p>
   * 2.1 The Entity Class:
   * "The entity class must have a no-arg constructor.
   * The entity class may have other constructors as well.
   * The no-arg constructor must be public or protected."
   */
  protected Playlist() {

  }

  public Playlist(String name, Collection<Song> songs) {
    this.name = name;
    this.songs = new HashSet<>(songs);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Playlist playlist)) return false;
    return Objects.equals(songs, playlist.songs) && Objects.equals(name, playlist.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(songs, name);
  }

  /**
   * Convenient for use as a <code>PathVariable</code> in endpoints.
   */
  @Id
  @GeneratedValue
  public Integer getId() {
    return id;
  }

  /**
   * This property (mutator) will be used dynamically by the JPA provider to populate this object
   * after construction via the no-args constructor.
   */
  protected void setId(Integer id) {
    this.id = id;
  }

  @ManyToMany
  public Set<Song> getSongs() {
    return songs;
  }

  public void setSongs(Collection<Song> songs) {
    this.songs = new HashSet<>(songs);
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
