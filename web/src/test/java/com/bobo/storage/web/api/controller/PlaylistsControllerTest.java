package com.bobo.storage.web.api.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistMother;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.TestConfig;
import com.bobo.storage.web.api.request.PlaylistsPostRequest;
import com.bobo.storage.web.api.request.PlaylistsPutNameRequest;
import com.bobo.storage.web.api.response.PlaylistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO [housekeeping] Annotate this with the @UnitTest for the Web module.
@WebMvcTest(PlaylistsController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaylistsControllerTest {

  // Mock Dependencies

  @MockitoBean
  private PlaylistService service;

  @MockitoBean
  private PlaylistQueryRepository playlists;

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
   * @see PlaylistsController#create(PlaylistsPostRequest)
   */
  @DisplayName("POST /playlists")
  @Nested
  class PostPlaylists {

    @Test
    void playlistCanBeCreatedWithoutSongs() throws Exception {
      // Given
      final int id = random.nextInt(100);

      PlaylistsPostRequest request = new PlaylistsPostRequest("foo", null);
      String requestPayload = objectMapper.writeValueAsString(request);

      Playlist requestedPlaylist = request.toCreate();
      PlaylistMother.setId(requestedPlaylist, id);

      PlaylistResponse expectedResponse = new PlaylistResponse(requestedPlaylist);
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

      verify(service, times(1)).create(requestedPlaylist);
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

  /**
   * @see PlaylistsController#getPlaylist(int)
   */
  @DisplayName("GET /playlists/{id}")
  @Nested
  class GetPlaylist {

    @Test
    void thereIsAPlaylist() throws Exception {
      // Given
      Playlist playlist = new PlaylistMother(random).withAll().get();

      PlaylistResponse expectedResponse = new PlaylistResponse(playlist);
      String expectedPayload = objectMapper.writeValueAsString(expectedResponse);

      // Stubbing
      when(playlists.findById(anyInt())).thenReturn(Optional.of(playlist));

      // When
      mockMvc.perform(get("/api/v1/playlists/{id}", playlist.getId()))
             // Then
             .andExpect(status().isOk())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(expectedPayload));
    }

    @Test
    void thereIsNoPlaylist() throws Exception {
      // Given
      final int id = random.nextInt(100);

      // Stubbing
      when(playlists.findById(anyInt())).thenReturn(Optional.empty());

      // When
      mockMvc.perform(get("/api/v1/playlists/{id}", id))
             // Then
             .andExpect(status().isNotFound());
    }

  }

  /**
   * @see PlaylistsController#renamePlaylist(int, PlaylistsPutNameRequest)
   */
  @DisplayName("POST /playlists{id}/name")
  @Nested
  class PutPlaylistName {

    /**
     * TODO [api] Once there are actually business rules in place for the {@link PlaylistService},
     *  I may need to test that a {@code 200 Ok} is returned with a payload containing the {@code name} (?),
     *  in the even the {@code Service} has had to format it (trim, title case, etc.).
     */
    @Test
    void thereIsAPlaylistAndItIsRenamed() throws Exception {
      // Given
      final int id = random.nextInt(100);

      PlaylistMother mother = new PlaylistMother(random);
      Playlist playlist = mother.withAll().get();

      String arbitraryValidName = mother.get().getName();
      PlaylistsPutNameRequest request = new PlaylistsPutNameRequest(arbitraryValidName);
      String requestPayload = objectMapper.writeValueAsString(request);

      // Stubbing
      when(playlists.findById(anyInt())).thenReturn(Optional.ofNullable(playlist));
      // service#updateName(Playlist,String):void

      // When
      mockMvc.perform(put("/api/v1/playlists/{id}/name", id).contentType(MediaType.APPLICATION_JSON)
                                                            .content(requestPayload))
             // Then
             .andExpect(status().isNoContent())
             .andExpect(header().doesNotExist("content-type"));

      verify(service, times(1)).updateName(playlist, request.name());
    }

    /**
     * I think {@code 400 Bad Request} is more appropriate here than {@code 404 Not Found} because the target resource
     * is a {@code Playlist#name}, not the {@code Playlist} itself. If the target resource {@code #name} did not exist,
     * that would be fine because this is a {@code PUT}; it would be created and respond with {@code 201 Created}.
     * <p>
     * However, {@code #name} will always exist for a {@code Playlist} in our domain.
     * <p>
     * Requesting an update on a {@code Playlist} that doesn't exist would be a {@code 400 Bad Request},
     * because responding with {@code 404 Not Found} does not make sense for a {@code PUT}.
     * I would expect the server to create it, if it didn't exist.
     * The issue is not that {@code Playlist#name} (the target resource of the request) does not exist,
     * but that this is an invalid URI.
     * <p>
     * TODO [housekeeping] Use {@link ParameterizedTest} to programmatically test this for all sub-routes asserting
     *  a {@code Playlist} exists. The {@link ValueSource} would just be all these routes. However, I may need to
     *  think about if that would be worth it as I'd also need to optionally pass in the {@code Request} object.
     */
    @Test
    void thereIsNoPlaylist() throws Exception {
      // Given
      final int id = random.nextInt(100);

      String arbitraryValidName = new PlaylistMother(random).withNames().get().getName();
      PlaylistsPutNameRequest request = new PlaylistsPutNameRequest(arbitraryValidName);
      String requestPayload = objectMapper.writeValueAsString(request);

      // Stubbing
      when(playlists.findById(anyInt())).thenReturn(Optional.empty());

      // When
      mockMvc.perform(put("/api/v1/playlists/{id}/name", id).contentType(MediaType.APPLICATION_JSON)
                                                            .content(requestPayload))
             // Then
             .andExpect(status().isBadRequest());
    }

  }

  /**
   * The {@link PlaylistService#delete(Playlist)} is still slim. I don't have many cases to test against yet.
   * When it is revised with further words and potential exception cases, then I will need to update this test.
   *
   * @see PlaylistsController#deletePlaylist(int)
   */
  @DisplayName("DELETE /playlists/{id}")
  @Nested
  class DeletePlaylist {

    /**
     * {@code 204 No Content} seems the most appropriate here because I have no status
     * or messages to provide for this action currently.
     * <p>
     * I do wonder if I should then do a {@code GET} request and assert I get a {@code 404 Not Found},
     * but that would then be testing the {@link PlaylistService#delete(Playlist)} method.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc9110.html#section-9.3.5">HTTP Semantics > DELETE</a>
     * @see HttpStatus#NO_CONTENT
     */
    @Test
    void thereIsAPlaylistAndItIsDeleted() throws Exception {
      // Given
      final int id = random.nextInt(100);

      Playlist playlist = new PlaylistMother(random).withAll().withIds(() -> id).get();

      // Stubbing
      when(playlists.findById(anyInt())).thenReturn(Optional.of(playlist));
      // service#delete(Playlist): void

      // When
      mockMvc.perform(delete("/api/v1/playlists/{id}", id))
             // Then
             .andExpect(status().isNoContent())
             // TODO [api] Figure out the right way to test there is no returned content.
             .andExpect(header().doesNotExist("content-type"));

      verify(service, times(1)).delete(playlist);
    }

    @Test
    void thereIsNoPlaylist() throws Exception {
      // Given
      final int id = random.nextInt(100); // TODO [housekeeping, low] helper #id()?

      // Stubbing
      when(playlists.findById(anyInt())).thenReturn(Optional.empty());

      // When
      mockMvc.perform(delete("/api/v1/playlists/{id}", id))
             // Then
             .andExpect(status().isNotFound());
    }

  }

}