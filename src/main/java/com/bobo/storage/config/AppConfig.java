package com.bobo.storage.config;

import com.bobo.storage.App;
import com.bobo.storage.core.domain._DomainMarker;
import com.bobo.storage.core.resource._ResourceMarker;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackageClasses = App.class)
@EntityScan(basePackageClasses = _DomainMarker.class)
@EnableJpaRepositories(basePackageClasses = _ResourceMarker.class)
@EnableTransactionManagement
public class AppConfig extends SpringBootServletInitializer {

}
