package com.bobo.storage.core.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Playlist {

  /**
   * Convenient for use as a <code>PathVariable</code> in endpoints.
   */
  @Id
  @GeneratedValue
  public int id;

  @Column(nullable = false)
  public String name;

  @ManyToMany
  public Set<Song> songs = new HashSet<>();

  public Playlist() {

  }

  public Playlist(String name, Collection<Song> songs) {
    this.name = name;
    this.songs.addAll(songs);
  }

}
