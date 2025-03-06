package com.bobo.storage.core.domain;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Playlist {

  private Integer id;

  private Set<Song> songs;

  private String name;

  public Playlist() {

  }

  public Playlist(String name, Collection<Song> songs) {
    this.name = name;
    this.songs = new HashSet<>(songs);
  }

  /**
   * Convenient for use as a <code>PathVariable</code> in endpoints.
   */
  @Id
  @GeneratedValue
  public Integer getId() {
    return id;
  }

  @SuppressWarnings("unused")
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
