package com.bobo.storage.core.domain;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

/**
 * TODO There are now additional {@code Song} fields the {@code Mother} needs to produce,
 *  but as I am not sure about which fields will stay, I am not going to create them as yet.
 */
public class SongMother implements EntityMother<Song> {

  private final Random random;

  private Supplier<Integer> ids;

  private Supplier<String> urls;

  /**
   * Unless otherwise configured, a {@code SongMother} uses randomness to generate mock data.
   * If your test has an instance of {@link Random}, you should share it with the {@code SongMother}.
   * <p>
   * This is also provided if you need to {@code seed} the generation of the mock data for
   * reproducing failures.
   *
   * @param random to use when generating mock data.
   */
  public SongMother(Random random) {
    this.random = random;

    withUrls();
  }

  /**
   * While the default constructor is always provided,
   * prefer {@link SongMother#SongMother(Random)}  SongMother} where possible.
   */
  @SuppressWarnings("unused")
  public SongMother() {
    this(new Random());
  }

  @Override
  public Song get() {
    Song song = new Song();

    Integer id = Objects.isNull(ids) ? null : ids.get();
    String url = Objects.isNull(urls) ? "" : urls.get();

    song.setId(id);
    song.setUrl(url);

    return song;
  }

  @Override
  public SongMother withAll() {
    return this.withIds().withUrls();
  }

  @Override
  public Song setId(Song song) {
    return EntityMother.setId(song, random.nextInt());
  }

  @Override
  public SongMother withIds(Supplier<Integer> ids) {
    this.ids = ids;
    return this;
  }

  @Override
  public SongMother withIds() {
    return withIds(this.random::nextInt);
  }

  public SongMother withUrls(Supplier<String> urls) {
    this.urls = urls;
    return this;
  }

  public SongMother withUrls() {
    return this.withUrls(() -> Long.toString(random.nextLong()));
  }

}
