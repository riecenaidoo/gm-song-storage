package com.bobo.storage.web.api.v1.controller;

import com.bobo.semantic.UnitTest;
import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistMother;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.domain.SongMother;
import com.bobo.storage.core.resource.query.AssertedResourceNotFoundException;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.v1.request.PlaylistSongsPatchRequest;
import com.bobo.storage.web.api.v1.response.SongResponse;
import com.bobo.storage.web.semantic.PatchOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(PlaylistSongsController.class)
@UnitTest(PlaylistSongsController.class)
class PlaylistSongsControllerTest {

  // Mock Dependencies

  @MockitoBean
  private PlaylistService service;

  @MockitoBean
  private PlaylistQueryRepository playlists;

  /**
   * I actually didn't need this one in the test,
   * but as it was a dependency and I forgot to mock it the context failed to load.
   * It would be easier to test and keep this test from needing to be changed if I made a rule that the controller
   * should speak to a single interface, like {@code PlaylistSongsPort}. Someone would need to implement (adapt) it,
   * and they would need to wire the needed dependencies to fulfill the contract. I once had this, but rolled it back,
   * and now I may bring it back.
   * TODO [design] re-introduce {@code ControllerPort}.
   */
  @MockitoBean
  private SongQueryRepository songs;

  // Test Utilities

  private final MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final Random random = new Random();

  @Autowired
  PlaylistSongsControllerTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  /**
   * @see PlaylistSongsController#getSongs(int)
   */
  @DisplayName("GET /playlists/{id}/songs")
  @Nested
  class GetSongs {

    @Test
    void thereAreSongs() throws Exception {
      // Given
      Playlist playlist = new PlaylistMother(random).withIds().withSongs().get();

      SongResponse[] expectedResponse = playlist.getSongs().stream().map(SongResponse::new)
                                                .toArray(SongResponse[]::new);
      String expectedPayload = objectMapper.writeValueAsString(expectedResponse);

      // Stubbing
      when(playlists.get(playlist.getId())).thenReturn(playlist);

      // When
      mockMvc.perform(get("/api/v1/playlists/{id}/songs", playlist.getId()))
             // Then
             .andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(expectedPayload));
    }

    @Test
    void thereAreNoSongs() throws Exception {
      // Given
      Playlist playlist = new PlaylistMother(random).withIds().withSongs(Collections::emptySet).get();

      SongResponse[] expectedResponse = new SongResponse[0];
      String expectedPayload = objectMapper.writeValueAsString(expectedResponse);

      // Stubbing
      when(playlists.get(playlist.getId())).thenReturn(playlist);

      // When
      mockMvc.perform(get("/api/v1/playlists/{id}/songs", playlist.getId()))
             // Then
             .andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(expectedPayload));
    }

    /**
     * This test is pretty much identical for all sub-routes of {@code /playlists/{id}/...},
     * so if I ever figure out how to programmatically test them, remove this test.
     *
     * @see PlaylistsControllerTest.PutPlaylistName#thereIsNoPlaylist
     */
    @Test
    void assertedPlaylistDoesNotExist() throws Exception {
      // Given
      final int id = random.nextInt(100);

      // Stubbing
      when(playlists.get(id)).thenThrow(AssertedResourceNotFoundException.class);

      // When
      mockMvc.perform(get("/api/v1/playlists/{id}/songs", id))
             // Then
             .andExpect(status().isBadRequest());
    }

  }

  /**
   * @see PlaylistSongsController#updateSongs(int, PlaylistSongsPatchRequest)
   */
  @DisplayName("PATCH /playlists/{id}/songs")
  @Nested
  class PatchSongs {

    @Test
    void addSong() throws Exception {
      // Given
      Playlist playlist = new PlaylistMother(random).withIds().withNames().get();
      Integer id = playlist.getId();

      Song validSong = new SongMother(random).withUrls().get();
      String validUrl = validSong.getUrl();
      PlaylistSongsPatchRequest request = new PlaylistSongsPatchRequest(PatchOperation.ADD, List.of(validUrl));
      String payload = objectMapper.writeValueAsString(request);

      // Stubbing
      when(playlists.get(id)).thenReturn(playlist);

      // When
      mockMvc.perform(patch("/api/v1/playlists/{id}/songs", id).contentType(MediaType.APPLICATION_JSON)
                                                               .content(payload))
             // Then
             .andExpect(status().isOk())
             .andExpect(header().doesNotExist("content-type"));

      verify(service, times(1)).addSongs(any(Playlist.class), any());
    }

    @Test
    void removeSong() throws Exception {
      // Given
      Playlist playlist = new PlaylistMother(random).withIds().withSongs().withNames().get();
      Integer id = playlist.getId();

      Song songInPlaylist = playlist.getSongs().stream().findFirst().orElseThrow();
      String url = songInPlaylist.getUrl();
      PlaylistSongsPatchRequest request = new PlaylistSongsPatchRequest(PatchOperation.REMOVE, List.of(url));
      String payload = objectMapper.writeValueAsString(request);

      // Stubbing
      when(playlists.get(id)).thenReturn(playlist);

      // When
      mockMvc.perform(patch("/api/v1/playlists/{id}/songs", id).contentType(MediaType.APPLICATION_JSON)
                                                               .content(payload))
             // Then
             .andExpect(status().isOk())
             .andExpect(header().doesNotExist("content-type"));

      verify(service, times(1)).removeSongs(any(Playlist.class), any());
    }

  }

}