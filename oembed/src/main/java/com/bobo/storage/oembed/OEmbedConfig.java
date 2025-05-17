package com.bobo.storage.oembed;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@Configuration
public class OEmbedConfig {

  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    return builder.build();
  }

}
