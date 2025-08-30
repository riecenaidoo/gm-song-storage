package com.bobo.storage.core;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Technically an integration test, but serves as a "smoke" test. */
@ActiveProfiles("test")
@SpringBootTest(classes = CoreTestConfig.class)
class CoreContextTest {

	private static final Logger log = LoggerFactory.getLogger(CoreContextTest.class);

	@Test
	void contextLoads() {
		log.info("Core module context loads successfully.");
	}
}
