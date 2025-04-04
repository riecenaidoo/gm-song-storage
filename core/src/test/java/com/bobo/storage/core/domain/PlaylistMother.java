package com.bobo.storage.core.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class PlaylistMother implements Mother<Playlist> {

  private final Random random = new Random();

  private Supplier<Integer> ids;

  private Supplier<String> names;

  private Supplier<Collection<Song>> songs;

  public static Playlist setId(Playlist playlist, Integer id) {
    playlist.setId(id);
    return playlist;
  }

  @SuppressWarnings("unused")
  public static Playlist setId(Playlist playlist) {
    Random random = new Random();
    return PlaylistMother.setId(playlist, random.nextInt());
  }

  @Override
  public Playlist get() {
    Playlist playlist = new Playlist();

    Integer id = Objects.isNull(ids) ? null : ids.get();
    String name = Objects.isNull(names) ? "" : names.get();
    Collection<Song> songs = Objects.isNull(this.songs) ? Collections.emptySet() : this.songs.get();

    playlist.setId(id);
    playlist.setName(name);
    playlist.setSongs(songs);

    return playlist;
  }

  public PlaylistMother withIds(Supplier<Integer> ids) {
    this.ids = ids;
    return this;
  }

  @SuppressWarnings("unused")
  public PlaylistMother withIds() {
    return withIds(this.random::nextInt);
  }

  public PlaylistMother withNames(Supplier<String> names) {
    this.names = names;
    return this;
  }

  public PlaylistMother withNames() {
    return withNames(() -> Long.toString(this.random.nextLong()));
  }

  public PlaylistMother withSongs(Supplier<Collection<Song>> songs) {
    this.songs = songs;
    return this;
  }

  public PlaylistMother withSongs() {
    Supplier<Song> song = () -> new Song(Long.toString(this.random.nextLong()));
    Supplier<Collection<Song>> songs = () -> IntStream.range(1, this.random.nextInt(10))
                                                      .mapToObj((i) -> song.get())
                                                      .toList();
    return withSongs(songs);
  }

}