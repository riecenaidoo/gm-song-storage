package com.bobo.storage.core.domain;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class PlaylistSongMother implements EntityMother<PlaylistSong> {

  private final Random random;

  private Supplier<Integer> ids;

  private Supplier<Playlist> playlists;

  private Supplier<Song> songs;

  public PlaylistSongMother(Random random) {
    this.random = random;
  }

  /**
   * While the default constructor is always provided,
   * prefer {@link PlaylistSongMother#PlaylistSongMother(Random)} where possible.
   */
  @SuppressWarnings("unused")
  public PlaylistSongMother() {
    this(new Random());
  }

  @Override
  public PlaylistSong get() {
    PlaylistSong playlistSong = new PlaylistSong();

    Integer id = Objects.isNull(ids) ? null : ids.get();
    Playlist playlist = Objects.isNull(playlists) ? (playlists = new PlaylistMother(random)).get() : playlists.get();
    Song song = Objects.isNull(songs) ? (songs = new SongMother(random)).get() : songs.get();

    playlistSong.setId(id);
    playlistSong.setPlaylist(playlist);
    playlistSong.setSong(song);

    return playlistSong;
  }

  @Override
  public PlaylistSongMother withAll() {
    // Currently, no additional state that needs to be set.
    // The other fields are required and initialised by default in the get.
    return this;
  }

  @Override
  public PlaylistSong setId(PlaylistSong playlistSong) {
    return EntityMother.setId(playlistSong, random.nextInt());
  }

  @Override
  public PlaylistSongMother withIds(Supplier<Integer> ids) {
    this.ids = ids;
    return this;
  }

  @Override
  public PlaylistSongMother withIds() {
    return withIds(this.random::nextInt);
  }

  public PlaylistSongMother withPlaylists(Supplier<Playlist> playlists) {
    this.playlists = playlists;
    return this;
  }

  public PlaylistSongMother withPlaylists() {
    PlaylistMother playlists = new PlaylistMother(random);
    return withPlaylists(playlists);
  }

  public PlaylistSongMother withSongs(Supplier<Song> songs) {
    this.songs = songs;
    return this;
  }

  public PlaylistSongMother withSongs() {
    SongMother songs = new SongMother(random);
    return withSongs(songs);
  }

}