package com.bobo.storage;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * <ol>
 *   <li>
 *     <code>@ActiveProfiles</code> to use the <code>test</code> profile, which I believe is a default configuration from Spring.
 *     I am using this so the tests use <code>application-test.properties</code>.
 *   </li>
 *   <li>
 *     <code>@Transactional</code> is specified to configure tests to run within a transaction and be rolled back after the test is executed.
 *   </li>
 * </ol>
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@UnitTest
@SpringBootTest(classes = TestConfig.class)
@Transactional
public @interface IntegrationTest {

}
