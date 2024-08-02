package com.bobo.storage;

import com.bobo.storage.config.AppConfig;
import org.springframework.boot.SpringApplication;

public class App {

  public static void main(String[] args) {
    SpringApplication.run(AppConfig.class, args);
  }
}