package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.TestConfig;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import com.bobo.storage.web.api.response.PlaylistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* TODO [housekeeping]
    Annotate this with @UnitTest. I'd need access to utilities from the Core module to do so.
    I think I would add a test (lifetime?) dependency on the Core module's test jar (might need to package it too).
 */
@WebMvcTest(PlaylistsController.class)
class PlaylistsControllerTest {

  // Mock Dependencies

  @MockitoBean
  private PlaylistService service;

  @MockitoBean
  private PlaylistQueryRepository playlists;

  @MockitoBean
  private SongQueryRepository songs;

  // Test Utilities

  private final MockMvc mockMvc;

  @Autowired
  PlaylistsControllerTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void playlistCanBeCreatedWithoutSongs() throws Exception {
    final int id = 1;

    // Given
    PlaylistsCreateRequest request = new PlaylistsCreateRequest("foo", null);
    PlaylistResponse expectedResponse = new PlaylistResponse(id, "foo", List.of());

    ObjectMapper objectMapper = new ObjectMapper();
    String body = objectMapper.writeValueAsString(request);
    String expectedBody = objectMapper.writeValueAsString(expectedResponse);
    String expectedUri = String.format("%s/api/v1/playlists/%d", TestConfig.testSchemeAuthority(), id);

    // Stubbing; When
    /* TODO [housekeeping]
        PlaylistMother in the same (test) package as Playlist would give access to #setId, which I marked protected.
        For now, stubbing the getId() is good enough.
     */
    when(service.create(any())).thenAnswer(invocation -> {
      Playlist playlist = invocation.getArgument(0);
      playlist = spy(playlist);
      when(playlist.getId()).thenReturn(id);
      return playlist;
    });

    // Then
    /* TODO [housekeeping]
        This endpoint name needs to match the one in the controller, so there might be a time when I should pull
        out the API paths to some sort of configuration/standard location.
     */
    mockMvc.perform(post("/api/v1/playlists").with(TestConfig::testSchemeAuthority)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .content(body))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", expectedUri))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(expectedBody));
  }

}