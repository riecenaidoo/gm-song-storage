package com.bobo.storage.core.domain;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class SongMother implements Mother<Song> {

  private final Random random;

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

    String url = Objects.isNull(urls) ? "" : urls.get();

    song.setUrl(url);

    return song;
  }

  @Override
  public SongMother withAll() {
    return this.withUrls();
  }

  public SongMother withUrls(Supplier<String> urls) {
    this.urls = urls;
    return this;
  }

  public SongMother withUrls() {
    return this.withUrls(() -> Long.toString(random.nextLong()));
  }

}
