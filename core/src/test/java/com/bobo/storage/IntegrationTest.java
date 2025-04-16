package com.bobo.storage;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * <ol>
 *   <li>
 *     {@link Transactional} is specified to configure tests to run within a transaction and be rolled back after the test is executed.
 *   </li>
 * </ol>
 */
@Documented
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional
@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
public @interface IntegrationTest {

}
