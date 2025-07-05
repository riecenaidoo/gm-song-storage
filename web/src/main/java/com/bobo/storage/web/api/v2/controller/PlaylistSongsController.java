package com.bobo.storage.web.api.v2.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.domain.PlaylistSong;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.resource.query.PlaylistSongQueryRepository;
import com.bobo.storage.core.service.PlaylistSongService;
import com.bobo.storage.core.service.SongService;
import com.bobo.storage.web.api.v2.request.PlaylistSongsCreateRequest;
import com.bobo.storage.web.api.v2.response.PlaylistSongResponse;
import com.bobo.storage.web.semantic.ResourceController;
import java.net.URI;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/api/v2/playlists/{playlistId}/songs")
@ResourceController(resource = PlaylistSong.class, respondsWith = PlaylistSongResponse.class)
public class PlaylistSongsController {

	private final PlaylistSongQueryRepository playlistSongs;

	private final PlaylistQueryRepository playlists;

	private final PlaylistSongService service;

	private final SongService songService;

	public PlaylistSongsController(
			PlaylistSongQueryRepository playlistSongs,
			PlaylistQueryRepository playlists,
			PlaylistSongService service,
			SongService songService) {
		this.playlistSongs = playlistSongs;
		this.playlists = playlists;
		this.service = service;
		this.songService = songService;
	}

	/**
	 * Create a {@link Song} within a {@link Playlist}.
	 *
	 * @param playlistId of the {@code Playlist} to create the {@code Song} within.
	 * @param request to create a {@code Song}.
	 */
	@PostMapping
	public ResponseEntity<PlaylistSongResponse> createPlaylistSong(
			@PathVariable int playlistId, @RequestBody PlaylistSongsCreateRequest request) {
		Playlist playlist =
				playlists
						.findById(playlistId)
						.orElseThrow(() -> new ResourceNotFoundException(Playlist.class, playlistId));
		Song song = request.toCreate();
		song = songService.create(song);

		PlaylistSong playlistSong = new PlaylistSong(playlist, song);
		playlistSong = service.create(playlistSong);
		PlaylistSongResponse response = new PlaylistSongResponse(playlistSong);
		URI resource =
				ServletUriComponentsBuilder.fromCurrentRequestUri()
						.path(String.format("/%d", playlistSong.getId()))
						.build()
						.toUri();
		return ResponseEntity.created(resource).body(response);
	}

	@GetMapping
	public ResponseEntity<PlaylistSongResponse[]> readPlaylistSongs(@PathVariable int playlistId) {
		Playlist playlist =
				playlists
						.findById(playlistId)
						.orElseThrow(() -> new ResourceNotFoundException(Playlist.class, playlistId));
		Collection<PlaylistSong> playlistSongs = this.playlistSongs.findAllByPlaylist(playlist);
		PlaylistSongResponse[] response =
				playlistSongs.stream().map(PlaylistSongResponse::new).toArray(PlaylistSongResponse[]::new);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removePlaylistSong(
			@PathVariable int playlistId, @PathVariable int id) {
		Playlist playlist =
				playlists
						.findById(playlistId)
						.orElseThrow(() -> new ResourceNotFoundException(Playlist.class, playlistId));
		PlaylistSong playlistSong =
				playlistSongs
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(PlaylistSong.class, id));
		if (!playlist.getId().equals(playlistSong.getPlaylist().getId())) {
			throw new SubresourceMismatchException(
					Playlist.class, playlist.getId(),
					PlaylistSong.class, playlistSong.getId());
		}
		service.delete(playlistSong);
		return ResponseEntity.noContent().build();
	}
}
