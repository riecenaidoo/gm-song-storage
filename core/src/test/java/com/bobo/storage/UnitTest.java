package com.bobo.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * If the {@code UnitTest} requires mocking, see {@link MockitoExtension}, {@link Mock}, {@link InjectMocks}
 * <pre>
 *   {@code @ExtendWith(MockitoExtension.class)}
 * </pre>
 * <br><hr><br>
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
 * <br><hr><br>
 * <ol>
 *   <li>TODO Configure JUnit</li>
 *   <li>TODO Implement a camel case test name formatter.</li>
 * </ol>
 */
@Documented
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public @interface UnitTest {

  /**
   * Optional, but recommended.
   * <p>
   * The naming convention of the {@code UnitTest} should be {@code UnitName+Test},
   * but supplying the {@code Class} in the annotation is a quick way to backlink to the test target,
   * so it shows up in IDE usage searches, etc.
   *
   * @return the {@code Class} this {@code UnitTest} is testing against.
   */
  Class<?> value() default Object.class;

}
