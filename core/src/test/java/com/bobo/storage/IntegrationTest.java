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
 *    {@link UnitTest} because the intention is to compose these annotations,
 *    with {@code UnitTest} being the base configuration and the rest building upon that configuration.
 *   </li>
 *   <li>
 *     {@link Transactional} is specified to configure tests to run within a transaction and be rolled back after the test is executed.
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
