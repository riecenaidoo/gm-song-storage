package com.bobo.semantic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A quick test that can be run to confirm the basic stability of the system.
 *
 * <p>We would typically run these after a build ({@code mvn compile}) so that we can fail fast
 * before attempting to run more exhaustive tests.
 *
 * <p>An example can be something as simple as confirming the application can still boot-up. For a
 * dependency injected application, this could be confirming the application context can load
 * without an issue.
 *
 * @implSpec Suffix with {@code Test}, even although they may involve several systems, they are not
 *     considered an {@link IntegrationTest}. They are intended to be run as part of the initial
 *     build-test cycle ({@code mvn test}, see also Maven Surefire plugin), but are also not
 *     considered a {@link UnitTest}.
 */
@Documented
@Target(ElementType.TYPE)
public @interface SmokeTest {}
