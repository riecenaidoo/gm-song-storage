package com.bobo.storage.web.api.v2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bobo.semantic.UnitTest;
import com.bobo.storage.core.domain.*;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.PlaylistSongQueryRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import com.bobo.storage.core.service.SongService;
import com.bobo.storage.web.TestConfig;
import com.bobo.storage.web.api.v2.request.SongsCreateRequest;
import com.bobo.storage.web.api.v2.response.PlaylistSongResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@TestInstance(
		TestInstance.Lifecycle
				.PER_CLASS) // TODO rm. Does not give the benefits I assumed. We don't need shared state.
@WebMvcTest(PlaylistSongsController.class)
@UnitTest(
		PlaylistSongsController
				.class) // In Spring, this is a Slice test, but that concept isn't universal - I think.
class PlaylistSongsControllerTest {

	// Mock Dependencies

	@MockitoBean private PlaylistSongQueryRepository playlistSongs;

	@MockitoBean private PlaylistQueryRepository playlists;

	@MockitoBean private PlaylistSongService service;

	@MockitoBean private SongService songService;

	// Test Utilities

	private final MockMvc mvc;

	private final ObjectMapper mapper;

	/**
	 * To be used for creating arbitrary values that will <b>never</b> change during the scope of the
	 * test they were created within.
	 *
	 * <p>Mark such values {@code final} where possible to declare the intention.
	 */
	private final Random random = new Random();

	@Autowired
	PlaylistSongsControllerTest(MockMvc mvc, ObjectMapper mapper) {
		this.mvc = mvc;
		this.mapper = mapper;
	}

	/**
	 * @see PlaylistSongsController#createPlaylistSong(int, SongsCreateRequest)
	 */
	@DisplayName("POST /playlists/{playlist_id}/songs")
	@Test
	void createPlaylistSong() throws Exception {
		// Given
		Playlist playlist = new PlaylistMother(random).withIds().get();

		SongMother songMother = new SongMother(random);
		String validUrl = songMother.withUrls().get().getUrl();
		SongsCreateRequest request = new SongsCreateRequest(validUrl);
		String requestPayload = mapper.writeValueAsString(request);

		// TODO figure out how to use PlaylistSong constructor
		final int id = random.nextInt(1, 101);
		PlaylistSongResponse expectedResponse =
				new PlaylistSongResponse(
						id, validUrl, Optional.empty(), Optional.empty(), Optional.empty());
		String expectedPayload = mapper.writeValueAsString(expectedResponse);
		String expectedURI =
				String.format(
						"%s/api/v2/playlists/%d/songs/%d",
						TestConfig.testSchemeAuthority(), playlist.getId(), id);

		// Stubbing
		/* TODO Reconsider the dependencies of this Controller. Am I stubbing too much?
				I don't think so, I'm really just setting ids tbh.
		*/
		when(playlists.findById(playlist.getId())).thenReturn(Optional.of(playlist));
		when(songService.create(request.toCreate()))
				.thenAnswer(
						invocation -> {
							Song song = invocation.getArgument(0);
							return songMother.setId(song);
						});
		when(service.create(any(PlaylistSong.class)))
				.thenAnswer(
						invocation -> {
							PlaylistSong playlistSong = invocation.getArgument(0);
							return EntityMother.setId(playlistSong, id);
						});

		// When
		mvc.perform(
						post("/api/v2/playlists/{playlist_id}/songs", playlist.getId(), id)
								.with(TestConfig::testSchemeAuthority)
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestPayload))
				// Then
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", expectedURI))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedPayload));

		verify(service, times(1)).create(any(PlaylistSong.class));
	}

	/**
	 * @see PlaylistSongsController#readPlaylistSongs(int)
	 */
	@Test
	void readPlaylistSongs() throws Exception {
		// Given
		Playlist playlist = new PlaylistMother(random).withIds().get();
		PlaylistSong[] allPlaylistSongs =
				new PlaylistSongMother(random)
						.withPlaylists(() -> playlist)
						.get(random.nextInt(1, 101))
						.toArray(PlaylistSong[]::new);
		PlaylistSongResponse[] expectedResponse =
				Arrays.stream(allPlaylistSongs)
						.map(PlaylistSongResponse::new)
						.toArray(PlaylistSongResponse[]::new);
		String expectedPayload = mapper.writeValueAsString(expectedResponse);

		// Stubbing
		when(playlists.findById(playlist.getId())).thenReturn(Optional.of(playlist));
		when(playlistSongs.findAllByPlaylist(playlist)).thenReturn(List.of(allPlaylistSongs));

		// When
		mvc.perform(get("/api/v2/playlists/{playlist_id}/songs", playlist.getId()))
				// Then
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedPayload));
	}

	/**
	 * @see PlaylistSongsController#removePlaylistSong(int, int)
	 */
	@Test
	@DisplayName("DELETE /playlists/{playlist_id}/songs/{id}")
	void removePlaylistSong() throws Exception {
		// Given
		PlaylistSong playlistSong = new PlaylistSongMother(random).withAll().get();
		int playlistId = playlistSong.getPlaylist().getId();
		int id = playlistSong.getId();

		// Stubbing
		when(playlists.findById(playlistId)).thenReturn(Optional.of(playlistSong.getPlaylist()));
		when(playlistSongs.findById(id)).thenReturn(Optional.of(playlistSong));

		// When
		mvc.perform(delete("/api/v2/playlists/{playlist_id}/songs/{id}", playlistId, id))
				// Then
				.andExpect(status().isNoContent())
				.andExpect(header().doesNotExist("content-type"));

		verify(service, times(1)).delete(playlistSong);
	}
}
