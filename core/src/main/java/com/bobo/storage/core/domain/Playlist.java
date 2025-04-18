package com.bobo.storage.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.*;

@Entity
public class Playlist extends DomainEntity {

  @Deprecated
  private Set<Song> songs;

  private String name;

  /**
   * @see DomainEntity#DomainEntity()
   */
  protected Playlist() {

  }

  @Deprecated
  public Playlist(String name, @Deprecated Collection<Song> songs) {
    this.name = name;
    this.songs = new HashSet<>(songs);
  }

  public Playlist(String name) {
    this(name, Collections.emptySet());
  }

  /**
   * Copy constructor.
   * <p>
   * The {@link TechnicalID} is never copied.
   *
   * @param playlist to copy.
   */
  public Playlist(Playlist playlist){
    this(playlist.getName(), playlist.getSongs());
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

  @Deprecated
  @ManyToMany
  public Set<Song> getSongs() {
    return songs;
  }

  @Deprecated
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
