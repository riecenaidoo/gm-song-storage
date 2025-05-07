package com.bobo.storage.core.domain;

import com.bobo.semantic.TechnicalID;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

/**
 * A {@link Song} within a {@link Playlist}.
 * <p>
 * Represents the relationship of the {@code Song} within the {@code Playlist},
 * which in future may contain information such as 'order', 'favourited', 'preferredVendor', etc.
 */
@Entity
public class PlaylistSong extends DomainEntity {

  private Playlist playlist;

  private Song song;

  /**
   * @see DomainEntity#DomainEntity()
   */
  protected PlaylistSong() {

  }

  /**
   * A {@code PlaylistSong} can only be created by adding a {@link Song} into a {@link Playlist}.
   *
   * @param playlist to add into. Must have an assigned {@link TechnicalID}.
   * @param song     to add. Must have an assigned {@link TechnicalID}.
   */
  public PlaylistSong(Playlist playlist, Song song) {
    // Existence Validation
    Objects.requireNonNull(playlist, "Playlist argument must not be null.");
    Objects.requireNonNull(song, "Song argument must not be null.");

    // State Validation
    if (playlist.getId() == null) {
      throw new IllegalArgumentException("Playlist argument must have an assigned TechnicalID.");
    }
    if (song.getId() == null) {
      throw new IllegalArgumentException("Song argument must have an assigned TechnicalID.");
    }

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
