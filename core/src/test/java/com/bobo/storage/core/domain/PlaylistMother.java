package com.bobo.storage.core.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class PlaylistMother implements EntityMother<Playlist> {

  private final Random random;

  private Supplier<Integer> ids;

  private Supplier<String> names;

  private Supplier<Collection<Song>> songs;

  /**
   * Unless otherwise configured, a {@code PlaylistMother} uses randomness to generate mock data.
   * If your test has an instance of {@link Random}, you should share it with the {@code PlaylistMother}.
   * <p>
   * This is also provided if you need to {@code seed} the generation of the mock data for
   * reproducing failures.
   *
   * @param random to use when generating mock data.
   */
  public PlaylistMother(Random random) {
    this.random = random;
  }

  /**
   * While the default constructor is always provided,
   * prefer {@link PlaylistMother#PlaylistMother(Random)} where possible.
   */
  @SuppressWarnings("unused")
  public PlaylistMother() {
    this(new Random());
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

  @Override
  public PlaylistMother withAll() {
    return this.withIds().withNames().withSongs();
  }

  @Override
  public Playlist setId(Playlist playlist) {
    return EntityMother.setId(playlist, random.nextInt());
  }

  @Override
  public PlaylistMother withIds(Supplier<Integer> ids) {
    this.ids = ids;
    return this;
  }

  @Override
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

  /**
   * @param songs typically a method reference to {@link Playlist#getSongs()},
   *              or used with {@code () -> constantValue} to create {@code Playlists} with the same {@code Songs}.
   */
  public PlaylistMother withSongs(Supplier<Collection<Song>> songs) {
    this.songs = songs;
    return this;
  }

  /**
   * @param songMother with a chained configuration to apply.
   * @param songsSize  supplier on an {@code Integer}, as this can accommodate both fixed and variable sized collections.
   */
  public PlaylistMother withSongs(SongMother songMother, Supplier<Integer> songsSize) {
    return withSongs(() -> songMother.get(songsSize.get()).toList());
  }

  public PlaylistMother withSongs() {
    return withSongs(new SongMother(random).withAll(), () -> this.random.nextInt(1, 10));
  }

}