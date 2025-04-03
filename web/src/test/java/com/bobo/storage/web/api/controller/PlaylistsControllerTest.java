package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@UnitTest // TODO Package Core tests to get access to the helper annotations, object creators?
@WebMvcTest(PlaylistsController.class)
class PlaylistsControllerTest {

  // Mock Dependencies

  @MockitoBean
  @SuppressWarnings("unused")
  private PlaylistService service;

  @MockitoBean
  @SuppressWarnings("unused")
  private PlaylistQueryRepository playlists;

  @MockitoBean
  @SuppressWarnings("unused")
  private SongQueryRepository songs;

  // Test Utilities

  private final MockMvc mockMvc;

  @Autowired
  PlaylistsControllerTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void playlistCanBeCreatedWithoutSongs() throws Exception {
    // Given
    PlaylistsCreateRequest request = new PlaylistsCreateRequest("foo", null);

    ObjectMapper objectMapper = new ObjectMapper();
    String body = objectMapper.writeValueAsString(request);

    // Using
    /*
      PlaylistMother in the same (test) package as Playlist would give access to #setId, which I marked protected.
      For now, stubbing the getId() is good enough.
     */
    when(service.create(any())).thenAnswer(invocation -> {
      Playlist playlist = invocation.getArgument(0);
      playlist = spy(playlist);
      when(playlist.getId()).thenReturn(1);
      return playlist;
    });

    // Then
    mockMvc.perform(post("/api/v1/playlists").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk());
  }

}