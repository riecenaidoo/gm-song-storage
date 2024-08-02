package com.bobo.storage.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Song {

  /**
   * Only one url needs to be saved. If its already in the DB, we don't need to persist it again. But the URL is the
   * id. Let's avoid technical IDs as far as we can and see how that goes.
   */
  @Id
  public String url;

  public Song() {
    // JPA. When Lombok, can replace with @NoArgs Constructor
  }

  /**
   * For easy deserialization from a JSON body which would hold these as <code>"songs" : ["url", "url"]</code>.
   */
  @SuppressWarnings("unusued") // JSON Deserialization, Also for Manual Deserialization
  public Song(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
