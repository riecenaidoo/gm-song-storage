package com.bobo.storage;

import com.bobo.storage.core.CoreContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 *
 * <ol>
 *   <li>It seems the easiest way to set up a {@link SpringBootTest} is to point it to a {@link
 *       SpringBootApplication}. For the {@code Core} module, I have no actual application, so I
 *       need a 'fake' on to target when setting up {@link SpringBootTest}(s).
 * </ol>
 */
@SpringBootApplication
@CoreContext
public class TestConfig {}
