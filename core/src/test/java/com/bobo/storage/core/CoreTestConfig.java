package com.bobo.storage.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @implNote The library modules do not have an actual {@link SpringBootApplication} to target when
 *     setting up {@link SpringBootTest}(s), so we create a "fake" target that spins up their
 *     context, as well as all autoconfiguration supplied by SpringBoot.
 */
@SpringBootApplication
@CoreContext
public class CoreTestConfig {}
