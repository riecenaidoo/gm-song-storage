package com.bobo.semantic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * A surrogate {@link SpringBootApplication} for Spring to bootstrap loading the application
 * context.
 *
 * <p>This is required for library modules that do not have an actual {@link SpringBootApplication}
 * for Spring to target when setting up {@link SpringBootTest}, or slice tests like {@link
 * DataJpaTest}, {@link WebMvcTest}.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication
public @interface TestApplication {}
