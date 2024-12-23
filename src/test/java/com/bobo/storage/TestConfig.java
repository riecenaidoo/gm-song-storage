package com.bobo.storage;

import com.bobo.storage.config.AppConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * <code>@SpringBootTest(classes = AppConfig.class)</code> to use the same configuration as the application when testing.
 * <p>
 * <code>@ActiveProfiles</code> to use the <code>test</code> profile, which I believe is a default configuration from Spring.
 * I am using this so the tests use <code>application-test.properties</code>.
 */
@SpringBootTest(classes = AppConfig.class)
@ActiveProfiles("test")
public interface TestConfig {

}
