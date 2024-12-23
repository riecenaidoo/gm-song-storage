package com.bobo.storage;

import com.bobo.storage.config.AppConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
/**
 * <ol>
 *   <li>
 *     <code>@SpringBootTest(classes = AppConfig.class)</code> to use the same configuration as the application when testing.
 *   </li>
 *   <li>
 *     <code>@ActiveProfiles</code> to use the <code>test</code> profile, which I believe is a default configuration from Spring.
 *     I am using this so the tests use <code>application-test.properties</code>.
 *   </li>
 *   <li>
 *     <code>@Transactional</code> is specified to configure tests to run within a transaction and be rolled back after the test is executed.
 *   </li>
 * </ol>
 */
@SpringBootTest(classes = AppConfig.class)
@ActiveProfiles("test")
@Transactional
public interface TestConfig {

}
