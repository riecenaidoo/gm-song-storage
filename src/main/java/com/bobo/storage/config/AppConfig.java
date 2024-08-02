package com.bobo.storage.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.bobo.storage")
@EntityScan(basePackages = "com.bobo.storage.domain")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.bobo.storage.resource")
public class AppConfig extends SpringBootServletInitializer {

}
