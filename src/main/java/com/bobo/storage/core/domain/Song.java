package com.bobo.storage.core.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Song {

  /**
   * Only one url needs to be saved. If its already in the DB, we don't need to persist it again. But the URL is the
   * id. Let's avoid technical IDs as far as we can and see how that goes.
   */
  @Id
  public String url;

  /**
   * Since we have defined a parameterised constructor, we do not get the default no args constructor
   * which is required for JPA.
   */
  @SuppressWarnings("unused")
  public Song() {
  }

  /**
   * For easy deserialization from a JSON body which would hold these as <code>"songs" : ["url", "url"]</code>.
   */
  @SuppressWarnings("unusued")
  public Song(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Song song = (Song) o;
    return Objects.equals(url, song.url);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(url);
  }

}
