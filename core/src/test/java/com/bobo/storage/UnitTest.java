package com.bobo.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * <ol>
 *   <li>
 *     {@code @ActiveProfiles} to use the {@code test} profile, which I believe is a default configuration from Spring.
 *     I am using this so the tests use {@code application-test.properties}.
 *   </li>
 *   <li>
 *     {@link TestInstance.Lifecycle#PER_CLASS} to override the default of {@link TestInstance.Lifecycle#PER_METHOD}.
 *     - I will not be using mutable test state, therefore I do not need to waste time recreating the test class.
 *     I will need to investigate how to run tests in parallel, in which case I would need to reconsider this choice.
 *     Also, while test state would be a smell, I would be using a {@link BeforeEach} to manage it.
 *   </li>
 * </ol>
 * <p>
 * TODO Configure JUnit
 * TODO Implement a camel case test name formatter.
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public @interface UnitTest {

}
