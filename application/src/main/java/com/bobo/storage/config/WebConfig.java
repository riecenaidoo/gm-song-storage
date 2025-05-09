package com.bobo.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <code>@EnableWebMvc</code> - see <code>WebMvcConfigurer</code>: Used to define cors mapping in <code>#addCorsMappings</code>.
 */
@EnableWebMvc
public class WebConfig extends SpringBootServletInitializer implements WebMvcConfigurer {

  @Value("${app.client.url}")
  private String clientUrl;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/v2/**")
            .allowedMethods("*")
            .allowedOrigins(clientUrl);
  }

}
