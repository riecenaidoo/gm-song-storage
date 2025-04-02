package com.bobo.storage;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * TODO Configure JUnit
 * TODO Implement a camel case test name formatter.
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
public @interface UnitTest {

}
