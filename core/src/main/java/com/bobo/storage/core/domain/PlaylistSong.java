package com.bobo.storage.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity
public class PlaylistSong extends DomainEntity {

  private Playlist playlist;

  private Song song;

  /**
   * @see DomainEntity#DomainEntity()
   */
  protected PlaylistSong() {

  }

  public PlaylistSong(Playlist playlist, Song song) {
    this.playlist = playlist;
    this.song = song;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PlaylistSong that)) return false;
    return Objects.equals(playlist, that.playlist) && Objects.equals(song, that.song);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playlist, song);
  }

  @ManyToOne(optional = false)
  public Playlist getPlaylist() {
    return playlist;
  }

  protected void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }

  @ManyToOne(optional = false)
  public Song getSong() {
    return song;
  }

  protected void setSong(Song song) {
    this.song = song;
  }

}
