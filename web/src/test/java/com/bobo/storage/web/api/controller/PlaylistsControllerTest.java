package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@UnitTest // TODO Package Core tests to get access to the helper annotations, object creators?
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
    // Given
    PlaylistsCreateRequest request = new PlaylistsCreateRequest("foo", null);
    PlaylistResponse expectedResponse = new PlaylistResponse(1, "foo", List.of());

    ObjectMapper objectMapper = new ObjectMapper();
    String body = objectMapper.writeValueAsString(request);
    String expectedBody = objectMapper.writeValueAsString(expectedResponse);

    // Stubbing; When
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
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(expectedBody));
  }

}