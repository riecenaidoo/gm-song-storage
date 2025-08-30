package com.bobo.storage;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Technically an integration test, but serves as a "smoke" test. */
@ActiveProfiles("test")
@SpringBootTest(classes = App.class)
class AppTest {

	private static final Logger log = LoggerFactory.getLogger(AppTest.class);

	@Test
	void contextLoads() {
		log.info("Application module context loads successfully.");
	}
}
