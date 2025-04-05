package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistMother;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.TestConfig;
import com.bobo.storage.web.api.request.PlaylistsCreateRequest;
import com.bobo.storage.web.api.response.PlaylistResponse;
import com.bobo.storage.web.api.response.SongResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO [housekeeping] Annotate this with the @UnitTest for the Web module.
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

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * To be used for creating arbitrary values that will <b>never</b> change during the scope of the test they were
   * created within.
   * <p>
   * Mark such values {@code final} where possible to declare the intention.
   */
  private final Random random = new Random();

  @Autowired
  PlaylistsControllerTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  /**
   * @see PlaylistsController#create(PlaylistsCreateRequest)
   */
  @DisplayName("POST /playlists")
  @Nested
  class PostPlaylists {

    @Test
    void playlistCanBeCreatedWithoutSongs() throws Exception {
      // Given
      final int id = random.nextInt(100);

      PlaylistsCreateRequest request = new PlaylistsCreateRequest("foo", null);
      String requestPayload = objectMapper.writeValueAsString(request);

      PlaylistResponse expectedResponse = new PlaylistResponse(id, "foo", new SongResponse[]{});
      String expectedPayload = objectMapper.writeValueAsString(expectedResponse);
      String expectedURI = String.format("%s/api/v1/playlists/%d", TestConfig.testSchemeAuthority(), id);

      // Stubbing
      when(service.create(any())).thenAnswer(invocation -> {
        Playlist playlist = invocation.getArgument(0);
        return PlaylistMother.setId(playlist, id);
      });

      // When
      /* TODO [housekeeping]
          This endpoint name needs to match the one in the controller, so there might be a time when I should pull
          out the API paths to some sort of configuration/standard location.
       */
      mockMvc.perform(post("/api/v1/playlists").with(TestConfig::testSchemeAuthority)
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(requestPayload))
             // Then
             .andExpect(status().isCreated())
             .andExpect(header().string("Location", expectedURI))
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(expectedPayload));
    }

  }

  /**
   * @see PlaylistsController#getPlaylists()
   */
  @DisplayName("GET /playlists")
  @Nested
  class GetPlaylists {

    @Test
    void thereArePlaylists() throws Exception {
      // Given
      final int numPlaylists = random.nextInt(1, 50);

      Set<Playlist> allPlaylists = new PlaylistMother(random).withAll().get(numPlaylists)
                                                             .collect(Collectors.toSet());

      PlaylistResponse[] expectedResponse = allPlaylists.stream().map(PlaylistResponse::new)
                                                        .toArray(PlaylistResponse[]::new);
      String expectedPayload = objectMapper.writeValueAsString(expectedResponse);

      // Stubbing
      when(playlists.findAll()).thenReturn(allPlaylists);

      // When
      mockMvc.perform(get("/api/v1/playlists"))
             // Then
             .andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(expectedPayload));
    }

    /**
     * Figuring out the status code for this case was interesting.
     * <p>
     * {@code 404 Not Found} was my first thought, but {@code 4xx} status codes are defined as {@code Client Error} class codes.
     * In this case, there is a resource at this location, but it is currently empty. It is not a client error,
     * and so I wondered if {@code 204 No Content} was appropriate.
     * <p>
     * After reading a bit further, I feel responding with {@code 204 No Content} would be an inaccuracy.
     * It is not meant for this use case, and there is content to return - an empty collection.
     * <p>
     * And so, I settled on this: The resource still exists, it is just empty.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9110.html#section-15.5.5">Internet Standard (RFC9110) HTTP Semantics > 404 Not Found</a>
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9110.html#section-15.3.5">Internet Standard (RFC9110) HTTP Semantics > 204 No Content</a>
     */
    @Test
    void thereAreNoPlaylists() throws Exception {
      // Given
      PlaylistResponse[] expectedResponse = new PlaylistResponse[0];
      String expectedPayload = objectMapper.writeValueAsString(expectedResponse);

      // Stubbing
      when(playlists.findAll()).thenReturn(Collections.emptySet());

      // When
      mockMvc.perform(get("/api/v1/playlists"))
             // Then
             .andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(expectedPayload));
    }

  }

}