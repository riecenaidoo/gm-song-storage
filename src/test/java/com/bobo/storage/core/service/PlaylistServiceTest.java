package com.bobo.storage.core.service;

import com.bobo.storage.TestConfig;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.access.PlaylistRepository;
import com.bobo.storage.core.resource.access.SongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

class PlaylistServiceTest implements TestConfig {

  private final PlaylistService playlistService;

  private final PlaylistRepository playlistRepository;

  private final SongRepository songRepository;

  //  ------ Testing Data ------

  private final Set<String> songURLs = Set.of("a", "b", "c");

  /**
   * While 'playlists' would be more conventional, I prefer how the usage reads with singular:
   * <code>playlist[0]</code>, <code>playlist[1]</code>.
   */
  private Playlist[] playlist;

  @Autowired
  PlaylistServiceTest(PlaylistService playlistService, PlaylistRepository playlistRepository, SongRepository songRepository) {
    this.playlistService = playlistService;
    this.playlistRepository = playlistRepository;
    this.songRepository = songRepository;
  }

  @BeforeEach
  void setUp() {
    playlists(2);
  }

  /**
   * Ensure that when writing this test we are saving <code>new</code> objects.
   * We do not want to be testing updates to a managed entity in the persistence context.
   */
  @Test
  void addingExistingSongsShouldNotCreateDuplicates() {
    playlist[0].setSongs(songs());
    playlist[1].setSongs(songs());

    playlistService.save(playlist[0]);
    playlistService.save(playlist[1]);

    Assertions.assertEquals(2, playlistRepository.findAll().size(), "Given");

    Assertions.assertEquals(songURLs.size(), songRepository.findAll().size());
  }

  @Test
  void updatingSongsInPlaylistWithExistingSongsShouldNotCauseDuplication() {
    playlist[0].setSongs(songs());
    playlistService.save(playlist[0]);

    playlistService.addSongs(playlist[0], songs());

    Assertions.assertEquals(1, playlistRepository.findAll().size(), "Given");

    Assertions.assertEquals(songURLs.size(), songRepository.findAll().size());
    Assertions.assertEquals(songURLs.size(), playlist[0].getSongs().size());
  }

  // ------ Testing Support ------

  /**
   * @param numOfPlaylists set up a number of <code>Playlist</code> object(s) that can be accessed via <code>playlist[...]</code>.
   */
  private void playlists(int numOfPlaylists) {
    playlist = new Playlist[numOfPlaylists];
    for (int i = 0; i < playlist.length; i++) {
      playlist[i] = new Playlist();
      playlist[i].setName("");
    }
  }

  /**
   * @return new <code>Song</code> objects from configured <code>songURLs</code>.
   */
  private Set<Song> songs() {
    return songURLs.stream().map(Song::new).collect(Collectors.toSet());
  }

}