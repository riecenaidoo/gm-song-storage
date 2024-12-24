package com.bobo.storage;

import com.bobo.storage.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The entrypoint to the application.
 * <p>
 * I prefer placing it in the root of our package structure,
 * because I feel it makes most sense there.
 * Placing it there is also helpful as we can use it as the target for component scanning.
 *
 * @see SpringBootApplication#scanBasePackageClasses()
 * @see ComponentScan#basePackageClasses()
 */
public class App {

  public static void main(String[] args) {
    SpringApplication.run(AppConfig.class, args);
  }

}