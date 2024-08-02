package com.bobo.storage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Playlist {

  /**
   * Convenient for use as a <code>PathVariable</code> in endpoints.
   */
  @Id
  public int id;

  @Column(nullable = false)
  public String name;

  @ManyToMany
  public Set<Song> songs;

}
