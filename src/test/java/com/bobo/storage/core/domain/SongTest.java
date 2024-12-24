package com.bobo.storage.core.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class SongTest {

  @Test
  void equality() {
    Song[] songs = new Song[3];
    songs[0] = new Song("a");
    songs[1] = new Song("a");
    songs[2] = new Song("b");

    Assertions.assertEquals(songs[0], songs[1]);
    Assertions.assertNotEquals(songs[1], songs[2]);
  }

  @Test
  void hashing() {
    Set<Song> songs = new HashSet<>();
    songs.add(new Song("a"));

    songs.add(new Song("a"));

    Assertions.assertEquals(1, songs.size());

    songs.add(new Song("b"));
    Assertions.assertEquals(2, songs.size());
  }

}