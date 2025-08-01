package com.bobo.storage.web.api.v2.controller;

import com.bobo.storage.core.domain.Playlist;
import com.bobo.storage.core.resource.query.PlaylistQueryRepository;
import com.bobo.storage.core.service.PlaylistService;
import com.bobo.storage.web.api.v2.request.PlaylistsCreateRequest;
import com.bobo.storage.web.api.v2.request.PlaylistsPatchRequest;
import com.bobo.storage.web.api.v2.response.PlaylistResponse;
import com.bobo.storage.web.semantic.ResourceController;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/api/v2/playlists")
@ResourceController(resource = Playlist.class, respondsWith = PlaylistResponse.class)
public class PlaylistsController {

	private final PlaylistQueryRepository playlists;

	private final PlaylistService service;

	public PlaylistsController(PlaylistService service, PlaylistQueryRepository playlists) {
		this.service = service;
		this.playlists = playlists;
	}

	@PostMapping
	public ResponseEntity<PlaylistResponse> createPlaylist(
			@RequestBody PlaylistsCreateRequest request) {
		Playlist playlist = request.toCreate();
		playlist = service.create(playlist);
		PlaylistResponse response = new PlaylistResponse(playlist);
		// TODO [design] I am beginning to see how I should define the Resource abstraction.
		URI resource =
				ServletUriComponentsBuilder.fromCurrentRequestUri()
						.path(String.format("/%d", playlist.getId()))
						.build()
						.toUri();
		return ResponseEntity.created(resource).body(response);
	}

	/**
	 * @param title an optional title fragment used as a case-insensitive search filter. If specified,
	 *     must not be blank (see {@link String#isBlank()}).
	 * @return zero or more {@code Playlists}.
	 */
	@GetMapping
	public ResponseEntity<PlaylistResponse[]> readPlaylists(@RequestParam Optional<String> title) {
		Collection<Playlist> playlists;
		if (title.isPresent()) {
			String search = title.get();
			if (search.isBlank()) {
				throw new RequestConstraintViolationException(
						"The 'title' parameter must not be blank if provided.");
			}
			playlists = this.playlists.findAllByNameContainingIgnoringCase(search);
		} else {
			playlists = this.playlists.findAll();
		}
		PlaylistResponse[] response =
				playlists.stream().map(PlaylistResponse::new).toArray(PlaylistResponse[]::new);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlaylistResponse> readPlaylist(@PathVariable int id) {
		Playlist playlist =
				playlists.findById(id).orElseThrow(() -> new ResourceNotFoundException(Playlist.class, id));
		PlaylistResponse response = new PlaylistResponse(playlist);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<PlaylistResponse> updatePlaylist(
			@PathVariable int id, @RequestBody PlaylistsPatchRequest request) {
		Playlist playlist =
				playlists.findById(id).orElseThrow(() -> new ResourceNotFoundException(Playlist.class, id));
		request.patch(playlist);
		playlist = service.update(playlist);
		PlaylistResponse response = new PlaylistResponse(playlist);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlaylist(@PathVariable int id) {
		Playlist playlist =
				playlists.findById(id).orElseThrow(() -> new ResourceNotFoundException(Playlist.class, id));
		service.delete(playlist);
		return ResponseEntity.noContent().build();
	}
}
