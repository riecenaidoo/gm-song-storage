package com.bobo.storage.oembed;

import com.bobo.semantic.UnitTest;
import com.bobo.storage.core.domain.Song;
import com.bobo.storage.core.resource.query.SongQueryRepository;
import com.bobo.storage.core.service.SongService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.mockito.Mockito.mock;

@ContextConfiguration(classes = {WebClientAutoConfiguration.class})
@ExtendWith(SpringExtension.class)
@UnitTest(OEmbedService.class)
class OEmbedServiceTest {

  private final OEmbedService service;

  @Autowired
  OEmbedServiceTest(WebClient.Builder builder) {
    this.service = new OEmbedService(builder.build(), mock(SongService.class), mock(SongQueryRepository.class));
  }

  /**
   * @see OEmbedService#lookupSong(Song)
   */
  @ParameterizedTest
  @ValueSource(strings = {
          "https://www.youtube.com/watch?v=rdwz7QiG0lk",
          // NOTE: Deezer's Share from App producing dzr.page.link does not work directly.
          // You need to send that request and look at the headers for "location".
          "https://www.deezer.com/us/track/350027801",
          "https://www.deezer.com/track/350027801?host=0&utm_campaign=clipboard-generic&utm_source=user_sharing&utm_content=track-350027801&deferredFl=1&universal_link=1",
          "https://open.spotify.com/track/6G6EAmGXX7T52zOWj2GWPE?si=145d2b7ff94641dc"
  })
  void lookupSong(String url) {
    Song song = new Song(url);
    Optional<OEmbedResponse> metadata = service.lookupSong(song);
    Assertions.assertTrue(metadata.isPresent());
    // TODO [test] define a control test for each provider to ensure a link for that provider works as expected.
    // TODO [test] see Deezer note above; create parameterized tests for each Provider for different link shapes.
  }


}