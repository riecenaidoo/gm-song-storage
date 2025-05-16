package com.bobo.storage.oembed;

import com.bobo.storage.core.domain.Song;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

@Service
public class OEmbedService {

  private final WebClient webClient;

  public OEmbedService(WebClient webClient) {
    this.webClient = webClient;
  }

  public Optional<Provider> lookupSong(Song song) {
    URL url;
    try {
      url = URI.create(song.getUrl()).toURL();
    } catch (MalformedURLException e) {
      // TODO [design] the Song URL may be stored as a String, but it should probably be typed to a URL.
      throw new RuntimeException("The URL of the Song was invalid.");
    }

    for (Provider provider : Provider.values()) {
      System.out.printf("\nPolling %s...", provider);

      URL query = provider.getQuery(url);
      URI queryURI;
      try {
        queryURI = query.toURI();
      } catch (URISyntaxException e) {
        // TODO [design] Should probably question whether #getQuery should return a URL or URI then.
        throw new RuntimeException("e");
      }

      Optional<OEmbedResponse> hit = webClient.get().uri(queryURI)
                                              .accept(MediaType.APPLICATION_JSON)
                                              .exchangeToMono(OEmbedService::poll)
                                              .map(Optional::of).defaultIfEmpty(Optional.empty())
                                              .block();

      if (hit != null && hit.isPresent()) {
        System.out.printf("\nRetrieved: \n%s\n", hit.get());
        return Optional.of(provider);
      }
    }

    return Optional.empty();
  }

  private static Mono<OEmbedResponse> poll(ClientResponse response) {
    if (response.statusCode().equals(HttpStatus.OK)) {
      System.out.println("HIT!");
      return response.bodyToMono(OEmbedResponse.class);
    } else {
      System.out.println("Miss.");
      return Mono.empty();
    }
  }

}
