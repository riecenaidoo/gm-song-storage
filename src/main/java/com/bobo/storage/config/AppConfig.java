package com.bobo.storage.config;

import com.bobo.storage.App;
import com.bobo.storage.core.domain._DomainMarker;
import com.bobo.storage.core.resource._ResourceMarker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <code>@EnableWebMvc</code> - see <code>WebMvcConfigurer</code>: Used to define cors mapping in <code>#addCorsMappings</code>.
 */
@SpringBootApplication(scanBasePackageClasses = App.class)
@EntityScan(basePackageClasses = _DomainMarker.class)
@EnableJpaRepositories(basePackageClasses = _ResourceMarker.class)
@EnableTransactionManagement
@EnableWebMvc
public class AppConfig extends SpringBootServletInitializer implements WebMvcConfigurer {

  @Value("${app.client.url}")
  private String clientUrl;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/v1/**").allowedOrigins(clientUrl);
  }

}
