package com.bobo.storage.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Playlist extends DomainEntity {

  private Set<Song> songs;

  private String name;

  /**
   * @see DomainEntity#DomainEntity()
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
