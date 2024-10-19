package com.bobo.storage.domain;

import javax.persistence.*;
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
  public Set<Song> songs;

}
