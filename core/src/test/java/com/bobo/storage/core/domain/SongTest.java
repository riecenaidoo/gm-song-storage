package com.bobo.storage.core.domain;

import com.bobo.semantic.UnitTest;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@UnitTest(Song.class)
class SongTest {

	@Test
	void equality() {
		Song[] songs = new Song[3];
		songs[0] = new Song("https://a.com");
		songs[1] = new Song("https://a.com");
		songs[2] = new Song("https://b.com");

		Assertions.assertEquals(songs[0], songs[1]);
		Assertions.assertNotEquals(songs[1], songs[2]);
	}

	@Test
	void hashing() {
		Set<Song> songs = new HashSet<>();
		songs.add(new Song("https://a.com"));

		songs.add(new Song("https://a.com"));

		Assertions.assertEquals(1, songs.size());

		songs.add(new Song("https://b.com"));
		Assertions.assertEquals(2, songs.size());
	}
}
